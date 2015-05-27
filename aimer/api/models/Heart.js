/**
 * Heart.js
 *
 * @description :: A existence of heart means the item has been loved by someone,
 * if this one is no more loved, there is no reason to heave a heart beating for it
 * @docs        :: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  connection: 'defaultLocalMongo',
  autoCreatedAt: true,
  autoUpdatedAt: true,
  autoPK: true,
  attributes: {
    user: {
      type: "string",
      required: true
    },
    artifact: {
      type: "string",
      required: true
    }
  }
};

