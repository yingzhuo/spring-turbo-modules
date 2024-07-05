package spring.turbo.module.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 应卓
 * @since 3.3.1
 */
public abstract class BigDecimalScalingSerializer extends StdSerializer<BigDecimal> {

    private final int scale;
    private final RoundingMode roundingMode;

    public BigDecimalScalingSerializer(int scale, RoundingMode roundingMode) {
        super(BigDecimal.class);
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value.setScale(scale, roundingMode));
    }

}
