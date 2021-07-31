package ar.edu.unsam.consorciovirtual.utils;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.domain.Gasto;
import ar.edu.unsam.consorciovirtual.domain.Rubro;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unsam.consorciovirtual.utils.Constants.CARPETA_DE_ARCHIVOS;

public class CreadorDePDF {
        private static final Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD);
        private static final Font tituloFontWhite = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, BaseColor.WHITE);
        private static final Font encabezadoFontWhite = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        private static final Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
        private static final Font categoryFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

        public static void createResumenDeExpensa(ExpensaDeDepartamento expensa, List<Gasto> gastos, String nombreArchivo,
                                                  List<Departamento> departamentos, Double importeComun, Double importeExtraordinario) {
                try {
                        Document document = new Document();
                        try {
                                PdfWriter.getInstance(document, new FileOutputStream(CARPETA_DE_ARCHIVOS+nombreArchivo));
                        } catch (FileNotFoundException fileNotFoundException) {
                                System.out.println("(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
                        }
                        document.addTitle("Resumen de expensas");
                        document.addAuthor("Consorcio Virtual");
                        document.addCreator("Consorcio Virtual");
                        document.open();

                        document.add(generarTitulo("Resumen De Expensas"));
                        document.add( Chunk.NEWLINE );

                        Image image;
                        try {
                                image = Image.getInstance(CARPETA_DE_ARCHIVOS+"logo.png");
                                image.setAbsolutePosition(400, 690);
                                document.add(image);
                        } catch (BadElementException ex) {
                                System.out.println("Image BadElementException" +  ex);
                        } catch (IOException ex) {
                                System.out.println("Image IOException " +  ex);
                        }

                        Paragraph encabezado = new Paragraph("\nDepartamento: "+ expensa.getUnidad()+"\n" +
                                "Período: "+ expensa.getPeriodo().toString()+"\n"+
                                "Importe Común: "+ expensa.getValorDepartamentoComun().toString()+"\n"+
                                "Importe Extraordinarias: "+ expensa.getValorDepartamentoExtraordinaria().toString()+"\n"+
                                "Importe Total: "+ expensa.getMontoAPagar().toString()+"\n"+
                                "Vencimiento: "+ LocalDate.now().plusDays(10).toString()+"\n\n\n", textoFont);
                        document.add(encabezado);
                        document.add(generarTitulo("Gastos del Período"));
                        document.add( Chunk.NEWLINE );

                        for(int i = 0; i< Rubro.values().length; i++) {
                                String nombreDeGasto = "";
                                switch(i){
                                        case 0: nombreDeGasto="Sueldo y Cargas Sociales";break;
                                        case 1: nombreDeGasto="Servicios Públicos";break;
                                        case 2: nombreDeGasto="Otros Servicios";break;
                                        case 3: nombreDeGasto="Mantenimiento Partes Comunes";break;
                                        case 4: nombreDeGasto="Reparaciones en Unidades";break;
                                        case 5: nombreDeGasto="Gastos Bancarios";break;
                                        case 6: nombreDeGasto="Limpieza";break;
                                        case 7: nombreDeGasto="Administración";break;
                                        case 8: nombreDeGasto="Otros Rubros";break;
                                        case 9: nombreDeGasto="Seguro";break;
                                }

                                Integer valorOrdinal = i;
                                List<Gasto> cantidadDeUnTipoDeGasto = gastos.stream().filter(x -> x.getRubro().ordinal() == valorOrdinal).collect(Collectors.toList());
                                Integer numColumns = 3;

                                PdfPTable table = new PdfPTable(numColumns);
                                table.setWidthPercentage(100);
                                table.setWidths(new int[]{60, 20, 20});
                                PdfPCell subTituloDeGasto = generarCeldaEncabezadoTabla("Gastos de "+ nombreDeGasto);
                                subTituloDeGasto.setColspan(3);
                                table.addCell(subTituloDeGasto);
                                table.addCell(generarCeldaEncabezado("Descripción"));
                                table.addCell(generarCeldaEncabezado("Tipo"));
                                table.addCell(generarCeldaEncabezado("Importe"));
                                table.setHeaderRows(2);

                                Double total = 0.0;
                                if(cantidadDeUnTipoDeGasto.size() == 0){
                                        table.addCell("");
                                        table.addCell("");
                                        table.addCell("");
                                }else{
                                        for (int row = 0; row < cantidadDeUnTipoDeGasto.size(); row++) {
                                                table.addCell(cantidadDeUnTipoDeGasto.get(row).getTitulo());
                                                table.addCell(generarCeldaCentradra(cantidadDeUnTipoDeGasto.get(row).getTipo()));
                                                table.addCell(generarCeldaDeImporte(cantidadDeUnTipoDeGasto.get(row).getImporte().toString()));
                                                total = total + cantidadDeUnTipoDeGasto.get(row).getImporte();
                                        }
                                        total=(double)Math.round(total* 100d) / 100d;
                                }

                                PdfPCell celdaFinal = new PdfPCell(new Paragraph("Total:"));
                                celdaFinal.setColspan(2);
                                table.addCell(celdaFinal);
                                table.addCell(generarCeldaDeImporte(total.toString()));

                                document.add(table);
                                document.add( Chunk.NEWLINE );
                        }

                        document.add(generarTitulo("Detalle General del Período"));
                        document.add( Chunk.NEWLINE );

                        Integer numColumns = 6;
                        PdfPTable table = new PdfPTable(numColumns);
                        table.setWidthPercentage(100);
                        table.setWidths(new int[]{10, 35, 10, 15, 15, 15});

                        table.addCell(generarCeldaEncabezado("Unidad"));
                        table.addCell(generarCeldaEncabezado("Propietario"));
                        table.addCell(generarCeldaEncabezado("%"));
                        table.addCell(generarCeldaEncabezado("Comúnes"));
                        table.addCell(generarCeldaEncabezado("Extraord."));
                        table.addCell(generarCeldaEncabezado("Total"));
                        table.setHeaderRows(1);

                       for (int row = 0; row < departamentos.size(); row++) {
                                Departamento departamento = departamentos.get(row);
                                Double expComunes = (double)Math.round(importeComun*departamento.getPorcentajeExpensa()) / 100d;
                                Double expExtra = (double)Math.round(importeExtraordinario*departamento.getPorcentajeExpensa()) / 100d;
                                Double total = (double)Math.round(expComunes+expExtra*100) / 100d;
                                table.addCell(departamento.getUnidad());
                                table.addCell(departamento.getNombrePropietario());
                                table.addCell(generarCeldaCentradra(departamento.getPorcentajeExpensa().toString()));
                                table.addCell(generarCeldaDeImporte(expComunes.toString()));
                                table.addCell(generarCeldaDeImporte(expExtra.toString()));
                                table.addCell(generarCeldaDeImporte(total.toString()));
                        }

                        PdfPCell celdaFinal = new PdfPCell(new Paragraph("Total:"));
                        celdaFinal.setColspan(3);
                        table.addCell(celdaFinal);
                        table.addCell(generarCeldaDeImporte(importeComun.toString()));
                        table.addCell(generarCeldaDeImporte(importeExtraordinario.toString()));
                        Double total = importeComun+importeExtraordinario;
                        table.addCell(generarCeldaDeImporte(total.toString()));

                        document.add(table);
                        document.close();
                        System.out.println("¡Se ha generado el resumen de expensas PDF!");
                } catch (DocumentException documentException) {
                        System.out.println("Se ha producido un error al generar un documento): " + documentException);
                }
        }

        public static void createReciboDeExpensa(ExpensaDeDepartamento expensa, String nombreArchivo) {
                try {
                        Document document = new Document(PageSize.A6.rotate());
                        try {
                                PdfWriter.getInstance(document, new FileOutputStream(CARPETA_DE_ARCHIVOS+nombreArchivo));
                        } catch (FileNotFoundException fileNotFoundException) {
                                System.out.println("(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
                        }
                        document.addTitle("Recibo de expensas");
                        document.addAuthor("Consorcio Virtual");
                        document.addCreator("Consorcio Virtual");
                        document.open();

                        document.add(generarTitulo("Recibo De Expensas"));
                        document.add( Chunk.NEWLINE );

                        Image image;
                        try {
                                image = Image.getInstance(CARPETA_DE_ARCHIVOS+"logo.png");
                                image.setAbsolutePosition(250, 15);
                                document.add(image);
                        } catch (BadElementException ex) {
                                System.out.println("Image BadElementException" +  ex);
                        } catch (IOException ex) {
                                System.out.println("Image IOException " +  ex);
                        }

                        Paragraph encabezado = new Paragraph("\nEl presente recibo da constancia que se registró,\n" +
                                "En el día: "+ LocalDate.now() +"\n"+
                                "El pago del Departamento: "+ expensa.getUnidad()+"\n" +
                                "Por un importe de: "+ expensa.getMontoAPagar().toString()+"\n"+
                                "Correspondiente a las expensas del período: "+ expensa.getPeriodo().toString()+"\n", textoFont);
                        document.add(encabezado);
                        document.add( Chunk.NEWLINE );
                        document.add(generarTitulo(""));

                        Paragraph cierre = new Paragraph("Recibo generado por medio de la aplicación: ", textoFont);
                        document.add(cierre);

                        document.close();
                        System.out.println("¡Se ha generado el recibo de expensas PDF!");
                } catch (DocumentException documentException) {
                        System.out.println("Se ha producido un error al generar un documento): " + documentException);
                }
        }

        private static PdfPTable generarTitulo(String texto){
                PdfPTable titulo = new PdfPTable(1);
                titulo.setWidthPercentage(100);
                PdfPCell columnHeader = new PdfPCell(new Phrase(texto, tituloFontWhite));
                columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                columnHeader.setBackgroundColor(BaseColor.BLACK);
                columnHeader.setPaddingBottom(10);
                titulo.addCell(columnHeader);

                return titulo;
        }

        private static PdfPCell generarCeldaEncabezado(String texto){
                PdfPCell columnHeader;
                columnHeader = new PdfPCell(new Phrase(texto, encabezadoFontWhite));
                columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                columnHeader.setBackgroundColor(BaseColor.GRAY);
                columnHeader.setPaddingBottom(4);
                return columnHeader;
        }

        private static PdfPCell generarCeldaEncabezadoTabla(String texto){
                PdfPCell columnHeader;
                columnHeader = new PdfPCell(new Phrase(texto, categoryFont));
                columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                columnHeader.setBackgroundColor(BaseColor.GRAY);
                columnHeader.setPaddingBottom(6);
                return columnHeader;
        }

        private static PdfPCell generarCeldaDeImporte(String texto){
                PdfPCell cell = new PdfPCell(new Phrase(texto));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingBottom(4);
                return cell;
        }

        private static PdfPCell generarCeldaCentradra(String texto){
                PdfPCell cell = new PdfPCell(new Phrase(texto));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingBottom(4);
                return cell;
        }

}