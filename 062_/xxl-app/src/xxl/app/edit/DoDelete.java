package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Delete command.
 */
class DoDelete extends Command<Spreadsheet> {

    DoDelete(Spreadsheet receiver) {
        super(Label.DELETE, receiver);
        addStringField("address", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
            _receiver.delete(stringField("address"));
        }catch(UnrecognizedEntryException e){
            throw new InvalidCellRangeException(e.getEntrySpecification());
        }
    }

}
