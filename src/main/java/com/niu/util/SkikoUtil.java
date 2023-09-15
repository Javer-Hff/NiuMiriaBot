package com.niu.util;

import org.jetbrains.skia.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Skiko绘图工具类
 *
 * @authoer:hff
 * @Date 2023/8/28 15:26
 */
public class SkikoUtil {
    public static boolean drawPornHub(String pre,String suf){
        //字体从系统字体种按名称匹配，匹配不到则使用系统默认字体
        Font font = new Font(Typeface.Companion.makeFromName("Cascadia Mono", FontStyle.Companion.getBOLD()), 100F);

        TextLine prefix = TextLine.Companion.make(pre, font);
        TextLine suffix = TextLine.Companion.make(suf, font);

        Paint black = new Paint();
        black.setARGB(0xFF, 0x00, 0x00, 0x00);
        Paint white = new Paint();
        white.setARGB(0xFF, 0xFF, 0xFF, 0xFF);
        Paint yellow = new Paint();
        yellow.setARGB(0xFF, 0xFF, 0x90, 0x00);

        Surface surface = Surface.Companion.makeRasterN32Premul((int) (prefix.getWidth() + suffix.getWidth() + 50), (int) (suffix.getHeight()+40));
        Canvas canvas = surface.getCanvas();
        canvas.clear(black.getColor());
        canvas.drawTextLine(prefix, 10F, 20 - font.getMetrics().getAscent(), white);

        float[] f = { 10F };
        RRect rpect = new RRect(prefix.getWidth() + 25, 15F, suffix.getWidth() + prefix.getWidth() + 25, suffix.getHeight() + 10,f);
        canvas.drawRRect(rpect, yellow);
        canvas.drawTextLine(suffix, prefix.getWidth() + 25, 20 - font.getMetrics().getAscent(), black);


        Image image = surface.makeImageSnapshot();
        Data data = image.encodeToData(EncodedImageFormat.PNG, 100);

        return drawToImage(data,"draw-porn.png");
    }

    public static boolean drawChoyen(String top,String bottom){
        //字体从系统字体种按名称匹配，匹配不到则使用系统默认字体
        Font sans = new Font(Typeface.Companion.makeFromName("Noto Sans SC", FontStyle.Companion.getBOLD()), 100F);
        Font serif = new Font(Typeface.Companion.makeFromName("Noto Serif SC", FontStyle.Companion.getBOLD()), 100F);

        TextLine red = TextLine.Companion.make(top, sans);
        TextLine silver = TextLine.Companion.make(bottom, serif);

        float width = Math.max(Objects.requireNonNull(red.getTextBlob()).getBlockBounds().getRight() + 70, Objects.requireNonNull(silver.getTextBlob()).getBlockBounds().getRight() + 250);

        Surface surface = Surface.Companion.makeRasterN32Premul(Float.valueOf(width).intValue(), 290);
        //top
        Canvas canvas = surface.getCanvas();
        canvas.skew(-0.45F,0F);
        float x = 70F;
        float y = 100F;

        Paint paint = new Paint();
        paint.setStroke(true);

        paint.setStrokeCap(PaintStrokeCap.ROUND);
        paint.setStrokeJoin(PaintStrokeJoin.ROUND);

        // 黒 22
        paint.setColor(Color.INSTANCE.makeRGB(0,0,0));
        paint.setStrokeWidth(22F);

        canvas.drawTextLine(red, x +4, y+4 ,paint);

        // 銀 20
        int[] rgbArr = {Color.INSTANCE.makeRGB(0, 15, 36),
                Color.INSTANCE.makeRGB(255, 255, 255),
                Color.INSTANCE.makeRGB(55, 58, 59),
                Color.INSTANCE.makeRGB(55, 58, 59),
                Color.INSTANCE.makeRGB(200, 200, 200),
                Color.INSTANCE.makeRGB(55, 58, 59),
                Color.INSTANCE.makeRGB(25, 20, 31),
                Color.INSTANCE.makeRGB(240, 240, 240),
                Color.INSTANCE.makeRGB(166, 175, 194),
                Color.INSTANCE.makeRGB(50, 50, 50)};
        float[] positionArr = {0.0F, 0.10F, 0.18F, 0.25F, 0.5F, 0.75F, 0.85F, 0.91F, 0.95F, 1F};
        paint.setShader(Shader.Companion.makeLinearGradient(
                0F,24F,0F,122F,rgbArr,positionArr
                ,GradientStyle.Companion.getDEFAULT()
        ));
        paint.setStrokeWidth(20F);
        canvas.drawTextLine(red,x+4,y+4,paint);

        //黑16
        paint.setShader(null);
        paint.setColor(Color.INSTANCE.makeRGB(0,0,0));
        paint.setStrokeWidth(16F);
        canvas.drawTextLine(red,x,y,paint);

        // 金 10
        rgbArr = new int[]{
                Color.INSTANCE.makeRGB(253, 241, 0),
                Color.INSTANCE.makeRGB(245, 253, 187),
                Color.INSTANCE.makeRGB(255, 255, 255),
                Color.INSTANCE.makeRGB(253, 219, 9),
                Color.INSTANCE.makeRGB(127, 53, 0),
                Color.INSTANCE.makeRGB(243, 196, 11)
        };
        positionArr = new float[]{0.0F, 0.25F, 0.4F, 0.75F, 0.9F, 1F};
        paint.setShader(Shader.Companion.makeLinearGradient(
                0F,20F,0F,100F,rgbArr,positionArr,GradientStyle.Companion.getDEFAULT()
        ));
        paint.setStrokeWidth(10F);
        canvas.drawTextLine(red,x,y,paint);

        // 黒 6
        paint.setShader(null);
        paint.setColor(Color.INSTANCE.makeRGB(0,0,0));
        paint.setStrokeWidth(6F);
        canvas.drawTextLine(red,x+2,y-3,paint);

        // 白 6
        paint.setShader(null);
        paint.setColor(Color.INSTANCE.makeRGB(255,255,255));
        paint.setStrokeWidth(6F);
        canvas.drawTextLine(red,x,y-3,paint);

        // 赤 4
        rgbArr = new int[]{
                Color.INSTANCE.makeRGB(255, 100, 0),
                Color.INSTANCE.makeRGB(123, 0, 0),
                Color.INSTANCE.makeRGB(240, 0, 0),
                Color.INSTANCE.makeRGB(5, 0, 0),
        };
        positionArr = new float[]{0.0F, 0.5F, 0.51F, 1F};
        paint.setShader(Shader.Companion.makeLinearGradient(
                0F,20F,0F,100F,rgbArr,positionArr,GradientStyle.Companion.getDEFAULT()
        ));
        paint.setStrokeWidth(4F);
        canvas.drawTextLine(red,x,y-3,paint);

        // 赤
        paint.setStroke(false);
        rgbArr = new int[]{
                Color.INSTANCE.makeRGB(230, 0, 0),
                Color.INSTANCE.makeRGB(123, 0, 0),
                Color.INSTANCE.makeRGB(240, 0, 0),
                Color.INSTANCE.makeRGB(5, 0, 0),
        };
        positionArr = new float[]{0.0F, 0.5F, 0.51F, 1F};
        paint.setShader(Shader.Companion.makeLinearGradient(
                0F,20F,0F,100F,rgbArr,positionArr,GradientStyle.Companion.getDEFAULT()
        ));
        canvas.drawTextLine(red,x,y-3,paint);



        //bottom
        x = 250F;
        y = 230F;
        Paint bottomPaint = new Paint();
        bottomPaint.setStroke(true);
        bottomPaint.setStrokeCap(PaintStrokeCap.ROUND);
        bottomPaint.setStrokeJoin(PaintStrokeJoin.ROUND);

        bottomPaint.setShader(null);
        bottomPaint.setColor(Color.INSTANCE.makeRGB(0,0,0));

        // 黒
        bottomPaint.setStrokeWidth(22F);
        canvas.drawTextLine(silver,x+5,y+2,bottomPaint);

        // 銀
        rgbArr = new int[]{
                Color.INSTANCE.makeRGB(0, 15, 36),
                Color.INSTANCE.makeRGB(250, 250, 250),
                Color.INSTANCE.makeRGB(150, 150, 150),
                Color.INSTANCE.makeRGB(150, 150, 150),

                Color.INSTANCE.makeRGB(25, 20, 31),
                Color.INSTANCE.makeRGB(240, 240, 240),
                Color.INSTANCE.makeRGB(166, 175, 194),
                Color.INSTANCE.makeRGB(50, 50, 50),
        };
        positionArr = new float[]{0.0F, 0.25F, 0.5F, 0.75F, 0.85F, 0.91F, 0.95F, 1F};
        bottomPaint.setShader(Shader.Companion.makeLinearGradient(
                0F,y-80,0F,y+18,rgbArr,positionArr,GradientStyle.Companion.getDEFAULT()
        ));
        paint.setStrokeWidth(19F);
        canvas.drawTextLine(silver,x+5,y+2,bottomPaint);

        // 黑
        bottomPaint.setShader(null);
        bottomPaint.setColor(Color.INSTANCE.makeRGB(16, 25, 58));
        bottomPaint.setStrokeWidth(17F);
        canvas.drawTextLine(silver,x,y,bottomPaint);

        // 白
        bottomPaint.setShader(null);
        bottomPaint.setColor(Color.INSTANCE.makeRGB(221, 221, 221));
        bottomPaint.setStrokeWidth(8F);
        canvas.drawTextLine(silver,x,y,bottomPaint);

        // 紺
        rgbArr = new int[]{
                Color.INSTANCE.makeRGB(16, 25, 58),
                Color.INSTANCE.makeRGB(255, 255, 255),
                Color.INSTANCE.makeRGB(16, 25, 58),
                Color.INSTANCE.makeRGB(16, 25, 58),
                Color.INSTANCE.makeRGB(16, 25, 58)
        };
        positionArr = new float[]{0.0F, 0.03F, 0.08F, 0.2F, 1F};
        bottomPaint.setShader(Shader.Companion.makeLinearGradient(
                0F,y-80,0F,y,rgbArr,positionArr,GradientStyle.Companion.getDEFAULT()
        ));
        bottomPaint.setStrokeWidth(7F);
        canvas.drawTextLine(silver,x,y,bottomPaint);

        // 银
        rgbArr = new int[]{
                Color.INSTANCE.makeRGB(245, 246, 248),
                Color.INSTANCE.makeRGB(255, 255, 255),
                Color.INSTANCE.makeRGB(195, 213, 220),
                Color.INSTANCE.makeRGB(160, 190, 201),
                Color.INSTANCE.makeRGB(160, 190, 201),
                Color.INSTANCE.makeRGB(196, 215, 222),
                Color.INSTANCE.makeRGB(255, 255, 255)
        };
        positionArr = new float[]{0.0F, 0.15F, 0.35F, 0.5F, 0.51F, 0.52F, 1F};
        bottomPaint.setShader(Shader.Companion.makeLinearGradient(
                0F,y-80,0F,y,rgbArr,positionArr,GradientStyle.Companion.getDEFAULT()
        ));
        bottomPaint.setStrokeWidth(5F);
        canvas.drawTextLine(silver,x,y-3,bottomPaint);



        Image image = surface.makeImageSnapshot();
        Data data = image.encodeToData(EncodedImageFormat.PNG, 200);

        return drawToImage(data,"draw-choyen.png");
    }

    public static String drawTextLine(List<String> textList){
        Font font = new Font(Typeface.Companion.makeFromName("Cascadia Mono", FontStyle.Companion.getBOLD()), 50F);

        List<TextLine> textLineList = textList.stream().map(o -> TextLine.Companion.make(o, font)).toList();
        TextLine MaxWidthTextLine = textLineList.stream().max((o1, o2) -> (int) (o1.getWidth() - o2.getWidth())).get();

        Paint black = new Paint();
        black.setARGB(0xFF, 0x00, 0x00, 0x00);
        Paint white = new Paint();
        white.setARGB(0xFF, 0xFF, 0xFF, 0xFF);

        Surface surface = Surface.Companion.makeRasterN32Premul((int) (MaxWidthTextLine.getWidth() + 50),
                (int) (MaxWidthTextLine.getHeight()*textLineList.size())+textLineList.size()*30);
        Canvas canvas = surface.getCanvas();
        canvas.clear(white.getColor());

        float h = 80F;
        for (TextLine line : textLineList) {
            canvas.drawTextLine(line, 10F, h, black);
            h+=100F;
        }

        Image image = surface.makeImageSnapshot();
        Data data = image.encodeToData(EncodedImageFormat.PNG, 100);
        String fileName = "draw-text.png";
        drawToImage(data,fileName);
        return fileName;
    }

    public static boolean drawToImage(Data data,String fileName){
        byte[] bytes = data.getBytes();
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
            outputStream.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
