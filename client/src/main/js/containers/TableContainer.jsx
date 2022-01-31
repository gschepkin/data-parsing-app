import React, {useEffect} from "react";
import Table from "../components/Table";
import {connect} from "react-redux";
import {updateCells} from "../redux/table_reducer";
import axios from "axios";

const TableContainer = (props) => {
    useEffect(() => {
        if (props.cells.length < 1)
            axios.get(`/api/cells`)
                .then(response => {
                    props.updateCells(response.data.cells)
                })
    });

    return <div>
        <Table {...props} />
    </div>
}

let mapStateToProps = (state, props) => {

    return {
        name: props.name,
        cells: state.data.cells,
        keys: props.keys,
        values: props.values
    }
}

export default connect(mapStateToProps, {updateCells})(TableContainer)