package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;


/**
 * Command for searching function names.
 */
class DoShowReferences extends Command<Spreadsheet> {

    DoShowReferences(Spreadsheet receiver) {
        super(Label.SEARCH_REFERENCES, receiver);
    }

    @Override
    protected final void execute() {
        _display.popup(_receiver.searchReferences());
    }

}