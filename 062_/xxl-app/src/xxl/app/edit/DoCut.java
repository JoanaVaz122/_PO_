package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Cut command.
 */
class DoCut extends Command<Spreadsheet> {

    DoCut(Spreadsheet receiver) {
        super(Label.CUT, receiver);
        addStringField("address", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try{    
            _receiver.cut(stringField("address"));
        }catch(UnrecognizedEntryException e){
            throw new InvalidCellRangeException(e.getEntrySpecification());
        }
    }
}
