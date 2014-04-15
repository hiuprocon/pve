package prototype;

import jp.sourceforge.acerola3d.a3.*;
import java.awt.*;
import java.util.ArrayList;

public class TextAreaComponent2D extends Component2D {
    final int LINE_COUNT;
    ArrayList<String> lines = new ArrayList<String>();
    public TextAreaComponent2D() {
        this(4);
    }
    public TextAreaComponent2D(int lineCount) {
        LINE_COUNT=lineCount;
        for (int i=0;i<LINE_COUNT;i++) {
            lines.add("> ");
        }
    }
    public void appendText(String s) {
        lines.remove(0);
        lines.add("> "+s);
    }
    public void clear() {
        lines.clear();
        for (int i=0;i<LINE_COUNT;i++) {
            lines.add("> ");
        }
    }

    @Override
    public void paint(Graphics2D g, A3CanvasInterface ci) {
        int x=30;
        int y=450 + LINE_COUNT*30;
        g.setColor(Color.WHITE);
        for (String line : lines) {
            g.drawString(line, x, y);
            y -= 30;
        }
    }

}
