import React from "react"
import {connect} from "react-redux"
import {Button, DatePicker, message, Space} from "antd"
import {updateDataSource, updateRange, updateSearchStatus} from "../redux/table_reducer";
import axios from "axios";
import moment from 'moment';

const {RangePicker} = DatePicker

const RangePickerContainer = (props) => {

    const changedRange = (range) => {
        props.updateRange(range !== null ? range : ['', ''])
    }

    const search = () => {
        let [start, end] = props.rangeStr
        let filtered = props.rangeStr.filter((value) => value !== '')
        props.updateSearchStatus(true)
        let promise = filtered.length > 1
            ? axios.get(`/api/cells_by_dates`, {params: {start, end}})
            : filtered.length > 0
                ? filtered.map((date) => axios.get(`/api/cell_for_date`, {params: {date}}))[0]
                : axios.get(`/api/cells`)
        promise.then(response => {
            if (response.data.failure === undefined) {
                message.success('The request is a success');
                props.updateRange([moment(response.data.start, "YYYY-MM-DD"), moment(response.data.end, "YYYY-MM-DD")])
                props.updateDataSource(response.data.cells !== undefined ? response.data.cells : [response.data])
            } else {
                message.error(response.data.failure);
                props.updateDataSource([])
            }
        })

    }

    return <Space style={{paddingBlock: "8px"}}>
        <RangePicker
            value={props.range}
            onCalendarChange={changedRange} />
        <Button
            type="primary"
            loading={props.searchStatus}
            onClick={search}>Search</Button>
    </Space>
}

let mapStateToProps = (state) => ({
    range: state.data.range,
    rangeStr: state.data.rangeStr,
    searchStatus: state.data.isSearch
})

export default connect(mapStateToProps, {
        updateDataSource,
        updateSearchStatus,
        updateRange
    }
)(RangePickerContainer)