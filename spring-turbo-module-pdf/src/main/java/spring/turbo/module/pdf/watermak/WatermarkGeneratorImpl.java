/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.pdf.watermak;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;
import spring.turbo.io.IOExceptionUtils;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author 应卓
 * @since 1.2.0
 */
public class WatermarkGeneratorImpl implements WatermarkGenerator {

    private final String watermarkFontName;
    private final int watermarkFontSize;
    private final String watermarkFontEncoding;

    private final boolean watermarkFontEmbedded;

    private final float watermarkFontOpacity;

    private final WatermarkPositions watermarkPositions;

    public WatermarkGeneratorImpl() {
        this("STSong-Light",
                56,
                "UniGB-UCS2-H",
                false,
                0.4F,
                WatermarkPositions.builder()
                        .add(300, 300, 30)
                        .build()
        );
    }

    public WatermarkGeneratorImpl(String watermarkFontName,
                                  int watermarkFontSize,
                                  String watermarkFontEncoding, boolean watermarkFontEmbedded,
                                  float watermarkFontOpacity,
                                  WatermarkPositions watermarkPositions) {
        this.watermarkFontName = watermarkFontName;
        this.watermarkFontSize = watermarkFontSize;
        this.watermarkFontEncoding = watermarkFontEncoding;
        this.watermarkFontEmbedded = watermarkFontEmbedded;
        this.watermarkFontOpacity = watermarkFontOpacity;
        this.watermarkPositions = watermarkPositions;
    }

    @Override
    public void addWatermark(Path in, Path out, Object watermarkContent) {
        try {
            final PdfReader reader = new PdfReader(Files.newInputStream(in));
            final PdfStamper stamper = new PdfStamper(reader, Files.newOutputStream(out));
            final PdfGState gs = new PdfGState();
            final BaseFont font = BaseFont.createFont(
                    this.watermarkFontName,
                    this.watermarkFontEncoding,
                    this.watermarkFontEmbedded
            );
            gs.setFillOpacity(this.watermarkFontOpacity);

            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;
            for (WatermarkPosition position : this.watermarkPositions) {
                for (int i = 1; i < total; i++) {
                    content = stamper.getOverContent(i);
                    content.beginText();
                    content.setGState(gs);
                    content.setColorFill(BaseColor.DARK_GRAY);
                    content.setFontAndSize(font, this.watermarkFontSize);
                    content.showTextAligned(Element.ALIGN_CENTER, watermarkContent.toString(), position.getX(), position.getY(), position.getRotation()); //水印内容和水印位置
                    content.endText();
                }
            }
            stamper.close();
        } catch (Exception e) {
            throw IOExceptionUtils.toUnchecked(e.getMessage());
        }
    }

}
