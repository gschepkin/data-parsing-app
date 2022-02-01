import React from "react";
import {connect} from "react-redux";
import {
    updateAverage,
    updateAverageStatus,
    updateDataSource,
    updateMinMax,
    updateMinMaxStatus,
    updateRange
} from "../redux/table_reducer";
import {Button, message, Space, Table} from "antd";
import {ReloadOutlined} from "@ant-design/icons";
import axios from "axios";

const TableContainer = (props) => {

    const condition = () => props.range.filter(value => value !== undefined && value !== '').length > 1

    const reloadAverage = () => {
        if (condition()) {
            props.updateAverageStatus(true)
            let [start, end] = props.range
            axios.get(`/api/average_price_by_dates`, {params: {start, end}}).then(response => {
                if (response.data.failure === undefined) {
                    message.success('The request is a success');
                    props.updateAverage(response.data.price)
                } else {
                    message.error(response.data.failure)
                    props.updateAverage(0)
                }

            })

        } else message.error("Please, change the dates-range")
    }
    const reloadMinMax = () => {
        if (condition()) {
            props.updateMinMaxStatus(true)
            let [start, end] = props.range
            axios.get(`/api/max_min_prices_by_dates`, {params: {start, end}}).then(response => {
                if (response.data.failure === undefined) {
                    message.success('The request is a success');
                    props.updateMinMax(response.data.min, response.data.max)
                } else {
                    message.error(response.data.failure)
                    props.updateMinMax(0, 0)
                }
            })

        } else message.error("Please, change the dates-range")
    }

    return <div>
        <Table
            columns={props.columns}
            dataSource={props.dataSource}
            pagination={false}
            scroll={{x: 1000, y: 500}}
            bordered
            summary={() => (
                <Table.Summary fixed>
                    <Table.Summary.Row>
                        <Table.Summary.Cell index={0}>Summary</Table.Summary.Cell>
                        <Table.Summary.Cell index={1}>Average</Table.Summary.Cell>
                        <Table.Summary.Cell index={2}><Space>
                            {props.average.value}
                            <Button
                                type="link"
                                icon={<ReloadOutlined />}
                                loading={props.average.loading}
                                disabled={props.average.disable}
                                onClick={reloadAverage}
                            />
                        </Space></Table.Summary.Cell>
                    </Table.Summary.Row>
                    <Table.Summary.Row>
                        <Table.Summary.Cell index={0}>{}</Table.Summary.Cell>
                        <Table.Summary.Cell index={1}>Min / Max</Table.Summary.Cell>
                        <Table.Summary.Cell index={2}><Space>
                            {props.minMax.min} / {props.minMax.max}
                            <Button
                                type="link"
                                icon={<ReloadOutlined />}
                                loading={props.minMax.loading}
                                disabled={props.minMax.disable}
                                onClick={reloadMinMax}
                            />
                        </Space></Table.Summary.Cell>
                    </Table.Summary.Row>
                </Table.Summary>
            )}
        />
    </div>

}

let mapStateToProps = (state) => {

    const columns = [
        {title: 'Start date', dataIndex: 'start'},
        {title: 'End date', dataIndex: 'end'},
        {title: 'Price', dataIndex: 'price'},
    ];

    const dataSource = state.data.dataSource.map((elem, index) => ({key: index, ...elem}))

    return {
        name: 'Processed data',
        dataSource: dataSource,
        columns: columns,
        minMax: state.data.bottomTable.minMax,
        average: state.data.bottomTable.average,
        range: state.data.rangeStr,
    }
}

export default connect(mapStateToProps, {
    updateDataSource,
    updateAverageStatus,
    updateMinMaxStatus,
    updateAverage,
    updateMinMax,
    updateRange,
})(TableContainer)