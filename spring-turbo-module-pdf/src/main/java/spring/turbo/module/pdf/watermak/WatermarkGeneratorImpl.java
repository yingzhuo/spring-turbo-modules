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

    /**
     * 水印字体名称
     */
    private String watermarkFontName = "STSong-Light";

    /**
     * 水印字体尺寸
     */
    private int watermarkFontSize = 56;

    /**
     * 水印字体字符集
     */
    private String watermarkFontEncoding = "UniGB-UCS2-H";

    /**
     * 水印字体是否嵌入
     */
    private boolean watermarkFontEmbedded = false;

    /**
     * 透明度
     */
    private float watermarkFontOpacity = 0.4F;

    /**
     * 水印位置
     */
    private WatermarkPositions watermarkPositions = WatermarkPositions.DEFAULT;

    public WatermarkGeneratorImpl() {
        super();
    }

    public void setWatermarkFontName(String watermarkFontName) {
        this.watermarkFontName = watermarkFontName;
    }

    public void setWatermarkFontSize(int watermarkFontSize) {
        this.watermarkFontSize = watermarkFontSize;
    }

    public void setWatermarkFontEncoding(String watermarkFontEncoding) {
        this.watermarkFontEncoding = watermarkFontEncoding;
    }

    public void setWatermarkFontEmbedded(boolean watermarkFontEmbedded) {
        this.watermarkFontEmbedded = watermarkFontEmbedded;
    }

    public void setWatermarkFontOpacity(float watermarkFontOpacity) {
        this.watermarkFontOpacity = watermarkFontOpacity;
    }

    public void setWatermarkPositions(WatermarkPositions watermarkPositions) {
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
            for (WatermarkPositions.Position position : this.watermarkPositions) {
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
