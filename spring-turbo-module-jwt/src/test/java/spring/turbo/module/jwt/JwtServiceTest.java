package spring.turbo.module.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.turbo.module.jwt.alg.JwtSignerFactories;

import java.time.Duration;

public class JwtServiceTest {

    @Test
    void test0() {
        var service = new JwtServiceImpl(JwtSignerFactories.createFromBase64EncodedString("3mDk7egxOtYe3oDEiZAhdZ2+ZdfPu8zsYtSl500l004="));

        var token = service.createToken(
                JwtData.newInstance()
                        .addPayload("name", "应卓")
                        .payloadExpiresAtFuture(Duration.ofHours(1L))
        );

        var result = service.validateToken(token);
        Assertions.assertEquals(ValidatingResult.OK, result);
    }

}
