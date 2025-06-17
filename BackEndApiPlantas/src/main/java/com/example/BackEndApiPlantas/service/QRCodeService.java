package com.example.BackEndApiPlantas.service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Service
public class QRCodeService {
    
    // Clase interna para devolver el resultado del QR en diferentes formatos
    public record QRCodeResult(byte[] bytes, String base64) {}
    
    public QRCodeResult generarQRCode(String data) {
        try {
            int width = 300;
            int height = 300;
            
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);
            
            String baseUrl = "https://2a84-139-47-52-187.ngrok-free.app/api/v1";  // Aquí defines la URL base directamente
            String fullData = baseUrl + "/lote/" + data; 

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(fullData, BarcodeFormat.QR_CODE, width, height, hints);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            
            String base64String = Base64.getEncoder().encodeToString(pngData);
            
            return new QRCodeResult(pngData, base64String);
            
        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Error al generar código QR", e);
        }
    }
}
