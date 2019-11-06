Vue.component('Toggle', {
  template: `
    <span class="toggle-wrapper" @click="toggle" role="checkbox" :aria-checked="value.toString()" tabindex="0" @keydown.space.prevent="toggle">
      <span class="toggle-background" :style="backgroundStyles"></span>
      <span class="toggle-indicator" :style="indicatorStyles"></span>
    </span>
  `,
  props: ['value'],
  methods: {
    toggle() {
      this.$emit('input', !this.value)
    }
  },
  computed: {
    backgroundStyles() {
      return {
        backgroundColor: this.value ? '#6059dc' : '#ded9e7'
      }
    },
    indicatorStyles() {
      return {transform: this.value ? 'translateX(2rem)' : 'translateX(0)'}
    }
  }
});

const axiosApi = axios.create({
  baseURL: '/api/'
});

new Vue({
  el: '#app',
  data: {
    team: null,
    bot: {
      shoutOutActive: false,
      shoutOutCount: 0
    },
    loading: true,
    errored: false
  },
  mounted() {
    this.loadTeamInfo();
    this.loadBotStatus();
  },
  methods: {
    updateShoutOutState(event) {
      axiosApi
        .post('botinfo', this.bot)
        .then(response => (console.log(response)))
        .catch(error => (console.log(error)));
    },
    refreshTeam(event) {
      axiosApi
        .post('refresh-team')
        .then(response => (this.loadTeamInfo()))
        .catch(error => (console.log(error)));
    },
    loadTeamInfo() {
      this.loading = true;
      axiosApi
        .get('teaminfo')
        .then(response => (this.team = response.data))
        .catch(error => {
          console.log(error);
          this.errored = true;
        })
        .finally(() => this.loading = false);
    },
    loadBotStatus() {
      axiosApi
        .get('botinfo')
        .then(response => (this.bot = response.data))
        .catch(error => (console.log(error)));
    }
  }
});
