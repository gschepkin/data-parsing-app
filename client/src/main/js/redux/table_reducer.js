const UPDATE_CELLS = "UPDATE_CELLS"

let initialState = {
    cells: []
}

export const table_reducer = (state = initialState, action) => {
    let stateCopy = {...state}

    switch (action.type) {
        case UPDATE_CELLS:
            stateCopy.cells = action.cells
            return stateCopy
        default:
            return state
    }
}

export const updateCells = (cells) => ({
    type: UPDATE_CELLS,
    cells: cells
})