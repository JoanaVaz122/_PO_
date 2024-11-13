package xxl.app.edit;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("address", Prompt.address());
        addStringField("content", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
            _receiver.insertContents(stringField("address"), stringField("content"));
        }catch(UnrecognizedEntryException e){
            if(e.getEntrySpecification().equals(stringField("address"))){
                throw new InvalidCellRangeException(stringField("address"));
            }else if(e.getEntrySpecification().equals(stringField("content"))){
                throw new UnknownFunctionException(stringField("content"));
            }
        }
    }
}
