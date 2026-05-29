const state = {
  isRunning: false,
  mode: 'feeding',
  startTime: null,
  elapsed: 0,
  timer: null
}

const mutations = {
  SET_RUNNING(state, val) { state.isRunning = val },
  SET_MODE(state, val) { state.mode = val },
  SET_START_TIME(state, val) { state.startTime = val },
  SET_ELAPSED(state, val) { state.elapsed = val },
  SET_TIMER(state, val) { state.timer = val }
}

const actions = {
  startTimer({ commit, state }, mode) {
    if (state.isRunning) return
    commit('SET_MODE', mode)
    commit('SET_START_TIME', new Date())
    commit('SET_ELAPSED', 0)
    commit('SET_RUNNING', true)
    const timer = setInterval(() => {
      commit('SET_ELAPSED', state.elapsed + 1)
    }, 1000)
    commit('SET_TIMER', timer)
  },
  pauseTimer({ commit, state }) {
    if (!state.isRunning) return
    clearInterval(state.timer)
    commit('SET_TIMER', null)
    commit('SET_RUNNING', false)
  },
  stopTimer({ commit, state }) {
    if (state.timer) {
      clearInterval(state.timer)
      commit('SET_TIMER', null)
    }
    commit('SET_RUNNING', false)
  },
  resetTimer({ commit, state }) {
    if (state.timer) {
      clearInterval(state.timer)
      commit('SET_TIMER', null)
    }
    commit('SET_RUNNING', false)
    commit('SET_START_TIME', null)
    commit('SET_ELAPSED', 0)
    commit('SET_MODE', 'feeding')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}