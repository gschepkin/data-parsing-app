const UPDATE_CELLS = "UPDATE_CELLS"
const UPDATE_RANGE = "UPDATE_RANGE"
const UPDATE_AVERAGE = "UPDATE_AVERAGE"
const UPDATE_MIN_MAX = "UPDATE_MIN_MAX"
const UPDATE_SEARCH_STATUS = "UPDATE_SEARCH_STATUS"
const UPDATE_AVERAGE_STATUS = "UPDATE_AVERAGE_STATUS"
const UPDATE_MIN_MAX_STATUS = "UPDATE_MIN_MAX_STATUS"

let initialState = {
    dataSource: [],
    range: [],
    rangeStr: ['',''],
    isSearch: false,
    bottomTable: {
        average: {
            value: 0,
            disable: false,
            loading: false
        },
        minMax: {
            min: 0,
            max: 0,
            disable: false,
            loading: false
        }
    }

}

export const table_reducer = (state = initialState, action) => {
    let stateCopy = {...state}

    switch (action.type) {
        case UPDATE_SEARCH_STATUS:
            stateCopy.isSearch = action.status
            return stateCopy

        case UPDATE_AVERAGE_STATUS:
            stateCopy.bottomTable = {...state.bottomTable}
            stateCopy.bottomTable.average = {...stateCopy.bottomTable.average.average}
            stateCopy.bottomTable.average.loading = action.status
            return stateCopy

        case UPDATE_MIN_MAX_STATUS:
            stateCopy.bottomTable = {...state.bottomTable}
            stateCopy.bottomTable.minMax = {...stateCopy.bottomTable.average.minMax}
            stateCopy.bottomTable.minMax.loading = action.status
            return stateCopy


        case UPDATE_CELLS:
            stateCopy.dataSource = action.dataSource
            stateCopy.isSearch = false
            stateCopy.bottomTable = {...state.bottomTable}
            stateCopy.bottomTable.average = {
                value: 0,
                disable: false,
                loading: false
            }
            stateCopy.bottomTable.minMax = {
                min: 0,
                max: 0,
                disable: false,
                loading: false
            }

            return stateCopy

        case UPDATE_RANGE:
            let toDate = (moment) => moment !== undefined && moment !== ''  ? moment.format("YYYY-MM-DD").toString() : ''
            stateCopy.range = action.range
            stateCopy.rangeStr = action.range.map(moment => toDate(moment))
            return stateCopy


        case UPDATE_AVERAGE:
            stateCopy.bottomTable = {...state.bottomTable}
            stateCopy.bottomTable.average = {
                value: action.average,
                disable: true,
                loading: false
            }
            return stateCopy

        case UPDATE_MIN_MAX:
            stateCopy.bottomTable = {...state.bottomTable}
            stateCopy.bottomTable.minMax = {
                min: action.min,
                max: action.max,
                disable: true,
                loading: false
            }
            return stateCopy

        default:
            return state
    }
}

export const updateDataSource = (dataSource) => ({
    type: UPDATE_CELLS,
    dataSource: dataSource
})

export const updateAverage = (average) => ({
    type: UPDATE_AVERAGE,
    average: average
})

export const updateMinMax = (min, max) => ({
    type: UPDATE_MIN_MAX,
    min: min,
    max: max
})

export const updateRange = (range) => ({
    type: UPDATE_RANGE,
    range: range
})

export const updateSearchStatus = (status) => ({
    type: UPDATE_SEARCH_STATUS,
    status: status
})

export const updateAverageStatus = (status) => ({
    type: UPDATE_AVERAGE_STATUS,
    status: status
})

export const updateMinMaxStatus = (status) => ({
    type: UPDATE_MIN_MAX_STATUS,
    status: status
})