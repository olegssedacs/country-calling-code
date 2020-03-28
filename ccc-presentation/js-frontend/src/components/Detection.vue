<template>
    <div class="detection">
        <h2>{{ title }}</h2>
        <div id="detection-form">
            <b-form @submit="onSubmit">
                <b-form-group
                        :invalid-feedback="invalidFeedback"
                        :state="state"
                        :valid-feedback="validFeedback"
                        id="phone-number-form-group"
                        label="International phone number"
                        label-for="phone-number-input-group">
                    <b-input-group id="phone-number-input-group"
                                   label="Phone number:"
                                   label-for="phone-number-input"
                                   prepend="+">
                        <b-form-input
                                id="phone-number-input"
                                placeholder="E.g. 12025550140 (US)"
                                required
                                type="number"
                                v-model="phoneNumber"
                        ></b-form-input>
                        <b-input-group-append>
                            <b-button type="submit" variant="primary">Submit</b-button>
                        </b-input-group-append>
                    </b-input-group>
                </b-form-group>
            </b-form>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {

        name: 'Search',
        props: {
            title: String
        },

        data() {
            return {
                form: {
                    phoneNumber: ''
                },
                requestBody: {
                    phoneNumber: ''
                },
                validationError: '',
                globalError: '',
                responseData: {
                    callingCode: 0,
                    countries: []
                }
            }
        },

        computed: {
            phoneNumber: {
                get() {
                    return this.form.phoneNumber;
                },
                set(val) {
                    this.form.phoneNumber = val
                    this.requestBody.phoneNumber = this.form.phoneNumber.toString()
                }
            },
            invalidFeedback() {
                if (this.validationError.length > 0) {
                    return 'Please enter a valid number. ' + this.validationError;
                }
                return 'Please come back in few minutes. Service is not reachable.'
            },
            validFeedback() {
                if (this.responseData.callingCode !== 0) {
                    let countries = this.responseData.countries.map(c => `${c.alpha2Code} ${c.name}`).join(', ');
                    let code = this.responseData.callingCode;
                    return `Code ${code} belongs to ${countries}`
                }
                return '';
            },
            state() {
                return this.validationError.length < 1 && this.globalError.length < 1
            }
        },

        methods: {
            onSubmit(evt) {
                evt.preventDefault()
                this.validationError = ''
                this.globalError = ''
                this.responseData.callingCode = 0
                this.responseData.countries = []
                console.log("sending request body : " + this.requestBody)
                axios.post('/v1/phone-numbers/detections', this.requestBody)
                    .then(
                        response => {
                            console.log('response data : ' + JSON.stringify(response.data.data))
                            this.responseData = response.data.data
                        },
                        e => {
                            let error = e.response.data.error;
                            if (error && error.errorType === 'VALIDATION_ERROR') {
                                this.validationError = error.validation[0].message;
                                console.log(`Validation failed : ${JSON.stringify(error)}`);
                            } else {
                                this.globalError = error ? error.message : `Service unreachable. code = ${e.response.status}`
                                console.error(`Error : ${this.globalError}`)
                            }
                        }
                    )
            },
        },
    }
</script>

<style scoped>

</style>
