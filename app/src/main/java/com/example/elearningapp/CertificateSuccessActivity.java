package com.example.elearningapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CertificateSuccessActivity extends AppCompatActivity {

    private Button btnDownloadCertificate;
    private TextView tvCertificateTitle, tvCertificateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_success);

        btnDownloadCertificate = findViewById(R.id.btnDownloadCertificate);
        tvCertificateTitle = findViewById(R.id.tvCertificateTitle); // Add in your XML
        tvCertificateDate = findViewById(R.id.tvCertificateDate);   // Add in your XML

        saveCertificationToFirestore(); // Save once user reaches here
        loadCertificationFromFirestore(); // Show certification details

        btnDownloadCertificate.setOnClickListener(v -> {
            try {
                generatePdfCertificate();
                Toast.makeText(this, "📄 Certificate PDF saved!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveCertificationToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        String userId = user.getUid();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        HashMap<String, Object> certData = new HashMap<>();
        certData.put("title", "Java & DSA Certification");
        certData.put("dateIssued", date);
        certData.put("certified", true);

        db.collection("users")
                .document(userId)
                .collection("certifications")
                .document("JavaDSA")
                .set(certData);
    }

    private void loadCertificationFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        String userId = user.getUid();

        db.collection("users")
                .document(userId)
                .collection("certifications")
                .document("JavaDSA")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        String dateIssued = documentSnapshot.getString("dateIssued");

                        tvCertificateTitle.setText("🎓 " + title);
                        tvCertificateDate.setText("📅 Issued on: " + dateIssued);
                    }
                });
    }
    private void generatePdfCertificate() throws IOException {
        String title = tvCertificateTitle.getText().toString();
        String date = tvCertificateDate.getText().toString();

        PdfDocument pdfDoc = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDoc.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 🌈 Background color
        paint.setColor(Color.parseColor("#FFFDE7")); // light yellow
        canvas.drawPaint(paint);

        // 🎓 Certificate Title
        paint.setColor(Color.BLACK);
        paint.setTextSize(28);
        paint.setFakeBoldText(true);
        canvas.drawText("Certificate of Completion", 150, 100, paint);

        // 📝 Subheading
        paint.setTextSize(18);
        paint.setFakeBoldText(false);
        canvas.drawText("This certifies that the learner has completed:", 100, 160, paint);

        // 🎯 Certification Name
        paint.setTextSize(20);
        paint.setColor(Color.parseColor("#0D47A1")); // Dark blue
        paint.setFakeBoldText(true);
        canvas.drawText(title, 120, 210, paint);

        // 📅 Date
        paint.setColor(Color.BLACK);
        paint.setTextSize(16);
        paint.setFakeBoldText(false);
        canvas.drawText(date, 120, 260, paint);

        // 🏅 Signature/Issuer Info
        paint.setTextSize(14);
        canvas.drawText("Issued by: E-Learning App", 120, 320, paint);

        pdfDoc.finishPage(page);

        // 🗂️ Save to app-specific external storage
        File certFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Certificates");
        if (!certFolder.exists()) {
            certFolder.mkdirs();
        }

        File file = new File(certFolder, "JavaDSA_Certificate.pdf");
        FileOutputStream fos = new FileOutputStream(file);
        pdfDoc.writeTo(fos);
        pdfDoc.close();
        fos.close();
    }

}
