package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.app.edit.InvalidCellRangeException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.Spreadsheet;

/**
 * Command for searching function names.
 */
class DoShowAll extends Command<Spreadsheet> {

    DoShowAll(Spreadsheet receiver) {
        super(Label.SHOW_ALL, receiver);
    }

    @Override
    protected final void execute() {
        _display.popup(_receiver.showAll());
    }

}