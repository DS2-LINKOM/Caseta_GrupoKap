package mx.linkom.caseta_grupokap.detectPlaca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DetectarPlaca {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap fechaHoraFoto(Bitmap foto) {
        Mat selected_image = new Mat(foto.getHeight(), foto.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(foto, selected_image);

        LocalDateTime hoy = LocalDateTime.now();

        int year = hoy.getYear();
        int month = hoy.getMonthValue();
        int day = hoy.getDayOfMonth();
        int hour = hoy.getHour();
        int minute = hoy.getMinute();
        int second = hoy.getSecond();

        String fecha = "";

        //Poner el cero cuando el mes o dia es menor a 10
        if (day < 10 || month < 10) {
            if (month < 10 && day >= 10) {
                fecha = year + "/0" + month + "/" + day;
            } else if (month >= 10 && day < 10) {
                fecha = year + "/" + month + "/0" + day;
            } else if (month < 10 && day < 10) {
                fecha = year + "/0" + month + "/0" + day;
            }
        } else {
            fecha = year + "-" + month + "-" + day;
        }

        String hora = "";

        if (hour < 10 || minute < 10) {
            if (hour < 10 && minute >= 10) {
                hora = "0" + hour + ":" + minute;
            } else if (hour >= 10 && minute < 10) {
                hora = hour + ":0" + minute;
            } else if (hour < 10 && minute < 10) {
                hora = "0" + hour + ":0" + minute;
            }
        } else {
            hora = hour + ":" + minute;
        }

        String segundos = "00";

        if (second < 10) {
            segundos = "0" + second;
        } else {
            segundos = "" + second;
        }

        //String text = fecha + " " + hora;


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String formattedDate = dateFormat.format(calendar.getTime());

        Log.e("FECHA", formattedDate);
        Log.e("FECHA", "Tamaño: " + formattedDate.length() + " menos 5: " + (formattedDate.length() - 6));

        String fechahora = "";

        for (int i = 0; i < formattedDate.length(); i++) {
            char c = formattedDate.charAt(i);

            if (i > (formattedDate.length() - 6)) {
                fechahora += (Character.isWhitespace(c) || !Character.isLetterOrDigit(c) && c != '.') ? "" : c;
            } else {
                fechahora += formattedDate.charAt(i);
            }

        }

        String text = fechahora;

        // Define the font face, scale, color, thickness, and line type
        int fontFace = Core.FONT_HERSHEY_SIMPLEX;
        double fontScale = 5.0;
        Scalar color = new Scalar(255, 0, 0); // White color
        int thickness = 25;
        int lineType = Imgproc.LINE_AA;

        // Define the text's baseline anchor point
        Point org = new Point(50, 300);
        Point org2 = new Point(50, 550);
        Point org3 = new Point(50, 800);

        // Draw the text on the image

        Imgproc.putText(selected_image, "LINK ACCESS", org, fontFace, fontScale, color, thickness, lineType, false);

        Imgproc.putText(selected_image, fecha, org2, fontFace, fontScale, color, thickness, lineType, false);

        Imgproc.putText(selected_image, hora, org3, fontFace, fontScale, color, thickness, lineType, false);


        Bitmap bitmap = Bitmap.createBitmap(selected_image.cols(), selected_image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(selected_image, bitmap);

        return bitmap;
    }


}