'use strict';

const DomainObjectService = require('./domain-object-service');
const NodeUtils = require('./node-service');

module.exports = {
    getConfig () {
        return process.env.APP_CONFIG;
    },
    getProperty (key) {
        if (!key) throw new Error('Key cannot be null/undefined');
        return DomainObjectService.getPropertyValue(
            this.getConfig(), key
        );
    },
    getRequiredProperty (key) {
        const value = this.getProperty(key);
        if (value) return value;
        if (!NodeUtils.isTest()) {
            throw new Error(`Missing required property: "${key}"`);
        }
    },
    getPort () {
        return this.getRequiredProperty('kalah-backend.port');
    },
    getBasePath () {
        return this.getRequiredProperty('kalah-backend.basePath');
    },
    getBaseUrl () {
        return this.getRequiredProperty('kalah-backend.baseUrl');
    },
    getWebSocketUrl() {
        return this.getRequiredProperty('kalah-websocket-server.url');
    }
};
