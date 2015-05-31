package curly.paperclip.paper

import rx.Observable

/**
 * @author Joao Pedro Evangelista
 */
interface PaperCommand {

    Observable<Optional<Paper>> getByItem(String item)
}
