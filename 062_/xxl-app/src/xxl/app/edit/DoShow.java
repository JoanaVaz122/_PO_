package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.app.edit.InvalidCellRangeException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.Spreadsheet;

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("address", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
            _display.popup(_receiver.showContent(stringField("address")));
        }catch(UnrecognizedEntryException e){
            throw new InvalidCellRangeException(e.getEntrySpecification());
        }
    }
}


