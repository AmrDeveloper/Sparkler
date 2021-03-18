package app.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

public class EditorSearch {

    private String mSearchQuery;
    private final RSyntaxTextArea mTextArea;
    private final SearchContext mSearchContext;

    public EditorSearch(RSyntaxTextArea textArea){
        mTextArea = textArea;
        mSearchContext = initSearchContext();
    }

    private SearchContext initSearchContext(){
        SearchContext searchContext = new SearchContext();
        searchContext.setMatchCase(true);
        searchContext.setRegularExpression(true);
        searchContext.setWholeWord(false);
        return searchContext;
    }

    public void startSearch(String query){
        mSearchQuery = query;
        mSearchContext.setSearchFor(query);
        mSearchContext.setSearchForward(true);
        boolean found = SearchEngine.find(mTextArea, mSearchContext).wasFound();
        if (!found) {
            System.out.println("Search not found");
        }
    }

    public void searchNextMatch(){
        if(mSearchQuery == null || mSearchQuery.isEmpty()){
            return;
        }
        mSearchContext.setSearchForward(true);
        boolean found = SearchEngine.find(mTextArea, mSearchContext).wasFound();
        if (!found) {
            System.out.println("Search not found");
        }
    }

    public void searchPrevMatch(){
        if(mSearchQuery == null || mSearchQuery.isEmpty()){
            return;
        }
        mSearchContext.setSearchForward(false);
        boolean found = SearchEngine.find(mTextArea, mSearchContext).wasFound();
        if (!found) {
            System.out.println("Search not found");
        }
    }
}
