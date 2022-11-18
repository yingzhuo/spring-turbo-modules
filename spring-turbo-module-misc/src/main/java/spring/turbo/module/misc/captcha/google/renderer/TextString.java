/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.google.renderer;

import java.util.ArrayList;

/**
 * @since 1.0.0
 */
public class TextString {

    private final ArrayList<TextCharacter> characters = new ArrayList<>();

    public void clear() {
        characters.clear();
    }

    public void addCharacter(TextCharacter tc) {
        characters.add(tc);
    }

    public ArrayList<TextCharacter> getCharacters() {
        return characters;
    }

    public double getWidth() {
        double minx = 0;
        double maxx = 0;
        boolean first = true;
        for (TextCharacter tc : characters) {
            if (first) {
                minx = tc.getX();
                maxx = tc.getX() + tc.getWidth();
                first = false;
            } else {
                if (minx > tc.getX()) {
                    minx = tc.getX();
                }
                if (maxx < tc.getX() + tc.getWidth()) {
                    maxx = tc.getX() + tc.getWidth();
                }
            }

        }
        return maxx - minx;
    }

    public double getHeight() {
        double miny = 0;
        double maxy = 0;
        boolean first = true;
        for (TextCharacter tc : characters) {
            if (first) {
                miny = tc.getY();
                maxy = tc.getY() + tc.getHeight();
                first = false;
            } else {
                if (miny > tc.getY()) {
                    miny = tc.getY();
                }
                if (maxy < tc.getY() + tc.getHeight()) {
                    maxy = tc.getY() + tc.getHeight();
                }
            }

        }
        return maxy - miny;
    }

}
