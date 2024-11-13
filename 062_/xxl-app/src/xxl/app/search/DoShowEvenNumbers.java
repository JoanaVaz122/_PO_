package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;


/**
 * Command for searching function names.
 */
class DoShowEvenNumbers extends Command<Spreadsheet> {

    DoShowEvenNumbers(Spreadsheet receiver) {
        super(Label.SEARCH_EVENNUMBERS, receiver);
    }

    @Override
    protected final void execute() {
        _display.popup(_receiver.searchEvenNumbers());
    }

}