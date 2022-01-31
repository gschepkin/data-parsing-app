import React from "react"
import css from "./App.module.css"
import TableContainer from "../containers/TableContainer";

const App = (props) => {
    return <div className={css.app}>
        <TableContainer
            name={'Processed data'}
            keys={(obj) => ['Start date', 'End date', 'Price']}
            values={(obj) => [obj.startDate, obj.endDate, obj.price]}
        />
    </div>
}

export default App