import React from "react"
import css from "./App.module.css"
import "./app.css"
import TableContainer from "../containers/TableContainer";
import RangePickerContainer from "../containers/RangePickerContainer";

const App = () => {
    return <div className={css.app}>
        <h2>Processed file</h2>
        <RangePickerContainer />
        <TableContainer />
    </div>
}

export default App