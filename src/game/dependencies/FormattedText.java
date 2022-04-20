package game.dependencies;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FormattedText {
    private Font font;

    public FormattedText(Font font){
        this.font = font;
    }

    public Text getTextFromString(String string){
        var text = new Text(string);
        text.setFont(this.font);
        return text;
    }
}
