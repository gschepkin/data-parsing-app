import {combineReducers, createStore} from "redux";
import {table_reducer} from "./table_reducer";

let reducers = combineReducers({
    data: table_reducer
});

let store = createStore(reducers);

window.store = store; // debug

export default store;