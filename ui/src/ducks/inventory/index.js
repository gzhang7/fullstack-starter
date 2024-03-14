import * as alerts from '../alerts/index'
import axios from 'axios'
import { createAction, handleActions } from 'redux-actions'

const actions = {
  INVENTORY_GET_ALL: 'inventory/get_all',
  INVENTORY_GET_PENDING: 'inventory/get_all_pending',
  INVENTORY_SAVE: 'inventory/save',
  INVENTORY_DELETE: 'inventory/delete',
  INVENTORY_REFRESH: 'inventory/refresh'
}

export let defaultState = {
  all: [],
  fetched: false,
}

export const findInventory = createAction(actions.INVENTORY_GET_ALL, () =>
  (dispatch, getState, config) => axios
    .get(`${config.restAPIUrl}/inventory`)
    .then((suc) => {
      dispatch(refreshInventory(suc.data))
      dispatch(alerts.openSuccess('Inventory created'))
    }
    )
)

export const saveInventory = createAction(actions.INVENTORY_SAVE, (inventory) =>
  (dispatch, getState, config) => axios
    .post(`${config.restAPIUrl}/inventory`, inventory)
    .then((suc) => {
      const invs = []
      getState().inventory.all.forEach(inv => {
        if (inv.id !== suc.data.id) {
          invs.push(inv)
        }
      })
      invs.push(suc.data)
      dispatch(refreshInventory(invs))
      dispatch(alerts.openSuccess('Inventory saved'))
    })
)

export const removeInventory = createAction(actions.INVENTORY_DELETE, (ids) =>
  (dispatch, getState, config) => axios
    .delete(`${config.restAPIUrl}/inventory`, { data: ids })
    .then((suc) => {
      const invs = []
      getState().inventory.all.forEach(inv => {
        if (!ids.includes(inv.id)) {
          invs.push(inv)
        }
      })
      dispatch(refreshInventory(invs))
      dispatch(alerts.openSuccess('Inventory removed'))
    })
)

export const refreshInventory = createAction(actions.INVENTORY_REFRESH, (payload) =>
  (dispatcher, getState, config) =>
    payload.sort((inventoryA, inventoryB) =>
      inventoryA.name < inventoryB.name ? -1 : inventoryA.name > inventoryB.name ? 1 : 0)
)

export default handleActions({
  [actions.INVENTORY_GET_PENDING]: (state) => ({
    ...state,
    fetched: false,
  }),
  [actions.INVENTORY_REFRESH]: (state, action) => ({
    ...state,
    all: action.payload,
    fetched: true,
  }),
  [actions.INVENTORY_GET_ALL]: (state, action) => ({
    ...state,
    fetched: true,
  }),
  [actions.INVENTORY_SAVE]: (state, action) => ({
    ...state,
    all: action.payload,
    fetched: true,
  }),
  [actions.INVENTORY_DELETE]: (state, action) => ({
    ...state,
    all: action.payload,
    fetched: true,
  })