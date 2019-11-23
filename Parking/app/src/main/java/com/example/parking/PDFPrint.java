package com.example.parking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFPrint extends AppCompatActivity {

Activity a;
String filename;

Vehicle V;

   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   PDFPrint(final String filename, Activity a,Vehicle V ){
        this.a=a;
        this.filename = filename;
        this.V = V;
        Dexter.withActivity(a)
               .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               .withListener(new PermissionListener() {
                   @Override
                   public void onPermissionGranted(PermissionGrantedResponse response) {

                       createPDFFILE(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+filename);

                   }

                   @Override
                   public void onPermissionDenied(PermissionDeniedResponse response) {

                   }

                   @Override
                   public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                   }
               })
               .check();






   }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   protected Document createPDFFILE(String path) {
        if (new File(path).exists())
            new  File(path).delete();
        try {
            Document document=new Document();
            // save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            // open to write
            document.open();
            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor(FirebaseController.userID);
            document.addCreator("Parking");
            //Font Setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize=20.0f;
            float valueFontSize=26.0f;

            //Custom font
            BaseFont fontName= BaseFont.createFont("assets/fonts/brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);
            //Create Title of Document
            Font orderNumberValueFont=new Font(fontName,valueFontSize,Font.NORMAL, BaseColor.BLACK);
            Font titleFont = new Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Parking Details", Element.ALIGN_CENTER,titleFont);
            //Add more
            Font orderNumberFont =new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,"Vehicle Number:",Element.ALIGN_LEFT,orderNumberFont);

            addNewItem(document, V.getVehicle_num(),Element.ALIGN_LEFT,orderNumberValueFont);
            addLineSeperator(document);
            addLineSeperator(document);

            addNewItem(document,"Arrival Time",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,V.getArrival_time(), Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);
            addNewItem(document,"Account Name",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,V.getName(), Element.ALIGN_LEFT,orderNumberValueFont);
            addLineSeperator(document);

            //Add Product  order detail
            addLineSpace(document);
            addNewItem(document,"Product Detail",Element.ALIGN_CENTER,titleFont);
            addLineSeperator(document);

            //Item 1
            addNewItemWithLeftAndRight(document,"Fee :","(-WON)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"Hour * "+Integer.toString(Settings.hour_fair),"---",titleFont,orderNumberValueFont);
            addLineSeperator(document);
            //Item 2
            addNewItemWithLeftAndRight(document,"Contact","+82",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,Settings.contact,"---",titleFont,orderNumberValueFont);

            //Item3
            addNewItemWithLeftAndRight(document,"Location","--",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,V.getLocation(),"---",titleFont,orderNumberValueFont);

            addLineSeperator(document);

            //Total
            addLineSpace(document);
            addLineSpace(document);


            document.close();
            Toast.makeText(a, "success", Toast.LENGTH_SHORT).show();

            printPDF();





            return document;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void  printPDF(){
        PrintManager printManager= (PrintManager)a.getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(PDFPrint.this,Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+filename);
            final PrintJob document = printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        }catch (Exception ex)
        {
            Log.e("EDMTDeV",""+ex.getMessage());

        }

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont,Font textRightFont) throws DocumentException {
    Chunk chunkTextLeft = new Chunk(textLeft,textLeftFont);
    Chunk chunkTextRight= new Chunk(textRight,textRightFont);
    Paragraph p = new Paragraph(chunkTextLeft);
    p.add(new Chunk(new VerticalPositionMark()));
    p.add(chunkTextRight);
        final boolean add;
        if (document.add(p)) add = true;
        else add = false;

    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);

    }

    private void addLineSpace(Document document) throws  DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk (text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
}
