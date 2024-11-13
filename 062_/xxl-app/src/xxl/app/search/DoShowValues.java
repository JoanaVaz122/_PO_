package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
        addStringField("value", Prompt.searchValue());
    }

    @Override
    protected final void execute() {
        _display.popup(_receiver.searchValue(stringField("value")));
    }

}
