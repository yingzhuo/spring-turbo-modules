/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.csv.reader.function;

/**
 * @author 应卓
 * @since 1.1.0
 */
public class SmartValueNormalizer implements GlobalValueNormalizer, ValueNormalizer {

    @Override
    public String normalize(String string) {
        if (string == null) {
            return null;
        }

        // 消除两头的白字符
        string = string.trim();
        // 消除两头的多余的双引号
        string = string.replaceFirst("\"*([^\"]*)\"*", "$1");

        return string;
    }

}
