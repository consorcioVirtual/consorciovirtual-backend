package ar.edu.unsam.consorciovirtual.domain;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CreadorDePDF {
        private static final Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD);
        private static final Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
        private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

        private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        private static final Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
        private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

        public static void createResumenDeExpensa(ExpensaDeDepartamento expensa, List<Gasto> gastos) {
                try {
                        Document document = new Document();
                        String nombreArchivo = "Expensa"+expensa.getPeriodo().toString()+"-"+expensa.getUnidad();
                        try {
                                PdfWriter.getInstance(document, new FileOutputStream("expensas/"+nombreArchivo+".pdf"));
                        } catch (FileNotFoundException fileNotFoundException) {
                                System.out.println("(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
                        }
                        document.addTitle("Resumen de expensas");
                        document.addAuthor("Consorcio Virtual");
                        document.addCreator("Consorcio Virtual");
                        document.open();

                        Chunk chunkTitulo = new Chunk("Resumen De Expensas\n", tituloFont);
                        Paragraph encabezado = new Paragraph("\nDepartamento: "+ expensa.getUnidad()+"\n" +
                                "Período: "+ expensa.getPeriodo().toString()+"\n"+
                                "Importe Común: "+ expensa.getValorDepartamentoComun().toString()+"\n"+
                                "Importe Extraordinarias: "+ expensa.getValorDepartamentoExtraordinaria().toString()+"\n"+
                                "Importe Total: "+ expensa.getMontoAPagar().toString()+"\n"+
                                "Vencimiento: "+ LocalDate.now().plusDays(10).toString() +"\n", textoFont);

                        document.add(chunkTitulo);
                        document.add(encabezado);

                        Chunk chunkTituloGastos = new Chunk("\nGastos del período:\n", tituloFont);
                        document.add(chunkTituloGastos);

                        for(int i=0;i<Rubro.values().length;i++) {
                                String nombreDeGasto = "";
                                switch(i){
                                        case 0: nombreDeGasto="Sueldo y cargas sociales";
                                                break;
                                        case 1: nombreDeGasto="Servicios Públicos";
                                                break;
                                        case 2: nombreDeGasto="Otros Servicios";
                                                break;
                                        case 3: nombreDeGasto="Mantenimiento Partes Comunes";
                                                break;
                                        case 4: nombreDeGasto="Reparaciones en Unidades";
                                                break;
                                        case 5: nombreDeGasto="Gastos Bancarios";
                                                break;
                                        case 6: nombreDeGasto="Limpieza";
                                                break;
                                        case 7: nombreDeGasto="Administración";
                                                break;
                                        case 8: nombreDeGasto="Otros";
                                                break;
                                        case 9: nombreDeGasto="Seguro";
                                                break;
                                }
                                Chunk chunkSubTituloDeGasto = new Chunk("\nGastos de "+ nombreDeGasto +":\n", categoryFont);
                                document.add(chunkSubTituloDeGasto);

                                Integer valorOrdinal = i;
                                List<Gasto> cantidad = gastos.stream().filter(x -> x.getRubro().ordinal() == valorOrdinal).collect(Collectors.toList());
                                Integer numColumns = 3;
                                Integer numRows = cantidad.size() * 3;

                                PdfPTable table = new PdfPTable(numColumns);
                                table.setWidthPercentage(100);
                                table.setWidths(new int[]{250, 75, 75});

                                PdfPCell columnHeader;
                                columnHeader = new PdfPCell(new Phrase("Descripción"));
                                columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                                table.addCell(columnHeader);
                                columnHeader = new PdfPCell(new Phrase("Tipo"));
                                table.addCell(columnHeader);
                                columnHeader = new PdfPCell(new Phrase("Importe"));
                                table.addCell(columnHeader);

                                table.setHeaderRows(1);

                                Double total = 0.0;
                                if(numRows == 0){
                                        table.addCell("");
                                        table.addCell("");
                                        table.addCell("");
                                }else{
                                        for (int row = 0; row < numColumns; row++) {
                                                table.addCell(cantidad.get(row).getTitulo());
                                                table.addCell(cantidad.get(row).getTipo());
                                                table.addCell(cantidad.get(row).getImporte().toString());
                                                total = total + cantidad.get(row).getImporte();
                                        }
                                        total=(double)Math.round(total* 100d) / 100d;
                                }

                                PdfPCell celdaFinal = new PdfPCell(new Paragraph("Total:"));
                                celdaFinal.setColspan(2);
                                table.addCell(celdaFinal);
                                table.addCell(total.toString());

                                document.add(table);
                        }
                        document.close();
                        System.out.println("¡Se ha generado tu hoja PDF!");
                } catch (DocumentException documentException) {
                        System.out.println("Se ha producido un error al generar un documento): " + documentException);
                }
        }

}
