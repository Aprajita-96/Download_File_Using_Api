package com.example.pdf.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/downloads")
public class PDFController {
    private static final String EXTERNAL_FILE_PATH = "C:/Users/AprajitaBajpai/Downloads/";

//method written for downloading file
    @RequestMapping("/file/{fileName:.+}")
    public ResponseEntity<ByteArrayResource> downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
                                                    @PathVariable("fileName") String fileName) throws IOException {
        File file = new File(EXTERNAL_FILE_PATH + fileName);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        if (file.exists()) {
            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
//method written for generating a file and converting that to pdf
    @RequestMapping(value="/pdfFile")
    public ResponseEntity<ByteArrayResource> generatePdf() throws DocumentException, FileNotFoundException {
        Document pdfDoc = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdfDoc, out);

        pdfDoc.open();

        Font myfont = new Font();
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(11);
        pdfDoc.add(new Paragraph("\n"));
        pdfDoc.addTitle(" Disclaimer");
        Paragraph para=new Paragraph("Loans are subjected to offence",myfont);
        para.setAlignment(Element.ALIGN_JUSTIFIED);
        pdfDoc.add(para);
        pdfDoc.close();



        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "loan.pdf" + "\"")
                .body(new ByteArrayResource(out.toByteArray()));

}


}

