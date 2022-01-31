import React from "react";
import css from "./Table.module.css";

/**
 * props:
 * @param name - the name of the table
 * @param data - cells of the table
 * @param header - array with names of cells of the table
 * @param keys - names and order of columns
 * @param values - values and order of columns
 * */
const Table = (props) => {

    const name = props.name.trim()
    const cells = props.cells
    const keys = (obj) => props.keys ? ['#'].concat(props.keys(obj)) : ['#'].concat(Object.keys(obj))
    const values = (index, obj) => props.values ? [index + 1].concat(props.values(obj)) : [index + 1].concat(Object.values(obj))

    const createKey = (elem, index) => <th key={index}>{elem}</th>

    const initRow = (elem, index) => <td key={`${index}-row-td`}>{elem}</td>
    const createRows = (cell, index) => <tr key={`${index}-row-tr`}>{values(index, cell).map(initRow)}</tr>

    const createKeys = cells.length > 0 ? keys(cells[0]).map(createKey) : <th></th>
    const createTable = cells.length > 0 ? cells.map(createRows) : <tr></tr>

    return <div className={css.table}>
        <div className={css.name}><h2>{name}</h2></div>
        <div className={css.table_wrap}>
            <table>
                <thead><tr className={css.table_header}>{createKeys}</tr></thead>
                <tbody className={css.table_body}>{createTable}</tbody>
            </table>
        </div>
    </div>
}

export default Table