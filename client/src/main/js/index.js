import React from "react"
import ReactDom from "react-dom"
import {BrowserRouter} from "react-router-dom"
import {Provider} from "react-redux"
import App from "./app/App"
import store from "./redux/redux_store"

ReactDom.render(
    <BrowserRouter>
        <Provider store={store}>
            <App />
        </Provider>
    </BrowserRouter>, document.getElementById('react')
)