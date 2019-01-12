package com.example.goldenman.myapplication;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class MainActivity extends AppCompatActivity {
    Button EncryptionBottom;
    Button DecryptionBottom;
    TextView EncryptedText;
    TextView timeToReadfromIot;
    TextView TotalProccingTimeTime;
    TextView testFileName;
    TextView TimeofEncrypting;
    TextView x1;
    TextView xx2;
    TextView x3;
    String totalTimeEncryption;
    TextView totalSumResults;
    RadioButton oneKb;
    RadioButton oneMB;
    RadioButton FourMB;
    RadioButton TenMB;
    String fileName;
    String totalTimereading;
    String totalTimewritingDB;
    TextView timeToStorinDatabase;
    ArrayList<String> stringArray = new ArrayList<String>();
    ArrayList<String> stringPullingArray = new ArrayList<String>();
    ArrayList<String> stringdecryptingArray = new ArrayList<String>();
    ArrayList<String> stringVeiwDecryption = new ArrayList<String>();
    FPE_Algo FPE;
    AssetManager assetManager;
    private static final String CLASS_NAME = MainActivity.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();


        // EncryptedText = (TextView) findViewById(R.id.viewCipherText);
        timeToReadfromIot = (TextView) findViewById(R.id.timeReadingFromXlsFile);
        TimeofEncrypting = (TextView) findViewById(R.id.timeEncrypting);
        EncryptionBottom = (Button) findViewById(R.id.encryptData);
        DecryptionBottom = (Button) findViewById(R.id.decryptData);
        TotalProccingTimeTime = (TextView) findViewById(R.id.totalEncryptionTime);
        timeToStorinDatabase = (TextView) findViewById(R.id.timeToStorinDatabase);
        oneKb = (RadioButton) findViewById(R.id.K1Radio);
        oneMB = (RadioButton) findViewById(R.id.M1Radio);
        FourMB = (RadioButton) findViewById(R.id.M4radio);
        TenMB = (RadioButton) findViewById(R.id.M10Radio);
        testFileName = (TextView) findViewById(R.id.fileTestName);
        totalSumResults = (TextView) findViewById(R.id.totalSum);
        x1 = (TextView) findViewById(R.id.aws);
        xx2 = (TextView) findViewById(R.id.readingXlsFile);
        x3 = (TextView) findViewById(R.id.encryptingTim);
        try{
            FPE = new FPE_Algo();
            FPE.init();
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error " + e, Toast.LENGTH_LONG).show();
            LOGGER.log(Level.SEVERE, "Error " + e);
        }

        EncryptionBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long lStartTime = System.nanoTime();
                    x1.setText("Time to Store in DataBase: ");
                    xx2.setText("Time read Data from Iot Device: ");
                    x3.setText("Time Encrypting: ");
                    readExcelFileFromAssets();
                    writeXLSFile();
                    timeCalc();

                    long lEndTime = System.nanoTime();
                    long output = lEndTime - lStartTime;

                    TotalProccingTimeTime.setText(String.valueOf(output / 1000000) + " ms");
                    Log.e("TotalProccingTimeTime", "*********************" + String.valueOf(output / 1000000) + "ms" + "***********");

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error " + e, Toast.LENGTH_LONG).show();
                    LOGGER.log(Level.SEVERE, "Error " + e);
                }
                stringArray.clear();
                stringPullingArray.clear();
                stringdecryptingArray.clear();
                stringVeiwDecryption.clear();
            }
        });


        DecryptionBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    long lStartTime = System.nanoTime();
                    x1.setText("Time to Store in Device: ");
                    xx2.setText("Time read From DataBase: ");
                    x3.setText("Decrypting Time: ");
                    readingForDecrypting();
                    writeDecryptedtoDB();
                    timeCalc();
                    long lEndTime = System.nanoTime();
                    long output = lEndTime - lStartTime;
                    TotalProccingTimeTime.setText(String.valueOf(output / 1000000));
                    Log.e("TotalProccingTimeTime", "*********************" + String.valueOf(output / 1000000) + "ms" + "***********");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error " + e, Toast.LENGTH_LONG).show();
                    LOGGER.log(Level.SEVERE, "Error " + e);
                }
                stringArray.clear();
                stringPullingArray.clear();
                stringdecryptingArray.clear();
                stringVeiwDecryption.clear();
            }
        });

        oneKb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testFileName.setText("1kb.xls");
                fileName = "1kb.xls";

            }
        });
        oneMB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testFileName.setText("1mb.xls");
                fileName = "1mb.xls";


            }
        });
        FourMB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testFileName.setText("4mb.xls");
                fileName = "4mb.xls";

            }
        });
        TenMB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testFileName.setText("10mb.xls");

                fileName = "10mb.xls";
            }
        });
    }

    public void mangoTest() {
        try {

            MongoClientURI uri = new MongoClientURI("mongodb://IbrahimSenan:gmsarah4ever@ds251737.mlab.com:51737");
            // MongoClientURI uri  = new MongoClientURI("https://api.mlab.com/api/1/databases/fpe_data_xls/collections/mobile_numbers?apiKey=1ga_Vg5xbU5pT0i4Kiv7mZCwTH1zQ6vR");
            MongoClient client = new MongoClient(uri);
            MongoDatabase db = client.getDatabase("fpe_data_xls");
            MongoCollection<BasicDBObject> collection = db.getCollection("mobile_numbers", BasicDBObject.class);
            BasicDBObject document = new BasicDBObject();
            document.put("name", "mkyong");
            document.put("age", 30);
            collection.insertOne(document);
            MongoCursor iterator = collection.find().iterator();
            //System.out.println("Insert successfully");

            while (iterator.hasNext()) {
                System.out.println(iterator.next());
                Log.e("FileUtils", "Cell Value: " + iterator.next());

            }

        } catch (Exception exception) {

        }
    }

    public void encryptActionPerformed(String plainTextForEncrypt) throws Exception {
        try {
            FPE = new FPE_Algo();
            // TODO add your handling code here:
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getAssets().open("file.txt")));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            String encrypted = FPE.fpe_encrypt(plainTextForEncrypt);
            stringPullingArray.add(encrypted);
            Log.e("Exception", "*********************" + encrypted + "***********");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "FRom " + e, Toast.LENGTH_LONG).show();
            LOGGER.log(Level.SEVERE, "Error " + e);
        }
    }

    private void decryptActionPerformed(String encryptedText) throws Exception {
        // TODO add your handling code here:
        FPE = new FPE_Algo();
        String encrypted = encryptedText.toString();
        String decrypted = FPE.fpe_decrypt(encrypted);
        stringdecryptingArray.add(decrypted);
        Log.e("decrypted", "decrypted Value: " + decrypted);
        //   de_time_2 = System.nanoTime();
        //    de_time = de_time_2 - de_time_1;
        //EncryptedText.setText("Decrypted: " + decrypted);
    }

    public void viewDecrypted() {
        for (String item : stringdecryptingArray
                ) {
            Log.e("Decrypted", "is: " + item);

        }
    }

    public void readExcelFileFromAssets() {
        long lStartTime = System.nanoTime();
        try {
            InputStream myInput;
            myInput = assetManager.open(fileName);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    myCell.setCellType(HSSFCell.ENCODING_UTF_16);
                    if (myCell.getColumnIndex() == 0) {
                        Log.e("FileUtils", "Cell Value: " + myCell.toString() + " Index :" + myCell.getColumnIndex());
                        //encryptActionPerformed();
                        stringArray.add(myCell.toString());
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // processing Time for Encryption Data
        setTimeofEncryptingData();
        long lEndTime = System.nanoTime();
        long output = lEndTime - lStartTime;
        timeToReadfromIot.setText(String.valueOf(output / 1000000) + "ms");
        totalTimereading = String.valueOf(output / 1000000);

        // return;
    }

    public void setTimeofEncryptingData() {
        long StartTimEncryption = System.nanoTime();
        try {
            for (String item : stringArray) {
                encryptActionPerformed(item);
            }

        } catch (Exception e) {
            Log.e("Exception", "*********************" + e + "***********");
        }
        long lEndTimeTimencryption = System.nanoTime();
        long outputTimencryption = lEndTimeTimencryption - StartTimEncryption;
        TimeofEncrypting.setText(String.valueOf(outputTimencryption / 1000000) + " ms");
        totalTimeEncryption = String.valueOf(outputTimencryption / 1000000);
    }

    public void writeXLSFile() {
        long lStartTime = System.nanoTime();
        try {


            boolean success = false;
            Workbook wb = new HSSFWorkbook();

            Cell c = null;

            //Cell style for header row
            CellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(HSSFColor.LIME.index);
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//New Sheet
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("myOrder");
            // Generate column headings
            int count = 0;
            while (stringPullingArray.size() > count) {

                Row row = sheet1.createRow(count);
                c = row.createCell(0);
                c.setCellValue(stringPullingArray.get(count));
                // c.setCellStyle(cs);
                sheet1.setColumnWidth(0, (15 * 500));
                count++;

            }
            File file = new File(this.getExternalFilesDir(null), "encrypted.xls");
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file" + file);
                success = true;
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception ex) {
                }
            }


        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error " + e);
        }
        long lEndTime = System.nanoTime();
        long output = lEndTime - lStartTime;
        timeToStorinDatabase.setText(String.valueOf(output / 1000000) + "ms");
        totalTimewritingDB = String.valueOf(output / 1000000);

    }

    //writing to decrypted data to db
    public void writeDecryptedtoDB() {
        long lStartTime = System.nanoTime();
        try {


            boolean success = false;
            Workbook wb = new HSSFWorkbook();
            Cell c = null;
            CellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(HSSFColor.LIME.index);
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("myOrder");
            // Generate column headings
            int count = 0;
            while (stringdecryptingArray.size() > count) {

                Row row = sheet1.createRow(count);
                c = row.createCell(0);
                Log.w("FileUtils", "Writing file" + stringdecryptingArray.get(count));
                c.setCellValue(stringdecryptingArray.get(count));
                // c.setCellStyle(cs);
                sheet1.setColumnWidth(0, (15 * 500));
                count++;

            }
            File file = new File(this.getExternalFilesDir(null), "decrypted.xls");
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file" + file);
                success = true;
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception ex) {
                }
            }


        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error " + e);
        }
        long lEndTime = System.nanoTime();
        long output = lEndTime - lStartTime;
        timeToStorinDatabase.setText(String.valueOf(output / 1000000) + "ms");
        totalTimewritingDB = String.valueOf(output / 1000000);

    }

    //reading from any Cloud for decrypted file
    public void readingForDecrypting() {
        long lStartTime = System.nanoTime();

        try {
            File file = new File(this.getExternalFilesDir(null), "encrypted.xls");
            FileInputStream myInput = new FileInputStream(file);
            testFileName.setText("encrypted.xls");
            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    myCell.setCellType(HSSFCell.ENCODING_UTF_16);
                    if (myCell.getColumnIndex() == 0) {
                        Log.e("FileUtils", "Cell Value: " + myCell.toString() + " Index :" + myCell.getColumnIndex());
                        stringVeiwDecryption.add(myCell.toString());
                        //encryptActionPerformed();
                        //stringArray.add(myCell.toString());
                    }

                }

            }

        } catch (FileNotFoundException e) {
            Log.w("FileUtils", "Error writing ", e);
        } catch (Exception e) {
            Log.w("FileUtils", "Error writing ", e);
        }
        setTimeofDecryptingData();
        long lEndTime = System.nanoTime();
        long output = lEndTime - lStartTime;
        timeToReadfromIot.setText(String.valueOf(output / 1000000) + "ms");
        totalTimereading = String.valueOf(output / 1000000);
    }

    //calculate time for decrypting file
    public void setTimeofDecryptingData() {
        long StartTimEncryption = System.nanoTime();
        try {
            for (String item : stringVeiwDecryption) {
                decryptActionPerformed(item);
            }

        } catch (Exception e) {
            Log.e("Exception", "*********************" + e + "***********");
        }
        long lEndTimeTimencryption = System.nanoTime();
        long outputTimencryption = lEndTimeTimencryption - StartTimEncryption;
        TimeofEncrypting.setText(String.valueOf(outputTimencryption / 1000000) + " ms");
        totalTimeEncryption = String.valueOf(outputTimencryption / 1000000);
    }


    public void timeCalc() {
        int TotalSum = Integer.parseInt(totalTimeEncryption) + Integer.parseInt(totalTimewritingDB) + Integer.parseInt(totalTimereading);
        Log.e("111", " " + totalTimeEncryption + " is Sum");
        Log.e("222", " " + totalTimewritingDB);
        Log.e("333", " " + totalTimereading);
        Log.e("Total Sum", "" + TotalSum + " is Sum");
        totalSumResults.setText(String.valueOf(TotalSum) + " ms");
    }

}
