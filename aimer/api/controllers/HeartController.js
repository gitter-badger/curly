/**
 * HeartController
 *
 * @description :: Server-side logic for managing hearts
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
  heart: function (req, res) {
    var user = req.param("user");
    var artifact = req.param("artifact");
    console.log("Requesting with user %s and artifact %s", user, artifact);
    return Heart.count(
      {
        user: user,
        artifact: artifact
      }).exec(function (err, result) {
        if (result === 1) {
          res.json(true)
        } else res.json(false)
      });
  },

  heartsByUser: function (req, res) {
    var user = req.param("user");
    sails.log.silly("Requesting hearts by user of user %s", user);

    return Heart.find().where({
      user: {"=": user}
    }).exec(function (err, data) {
      if (err) {
        return res.badRequest
      } else if (data === "undefined") {
        return res.notFound
      } else {
        res.json(data)
      }
    });
  },

  //remember to set application/json as accept header
  heartIt: function (req, res) {
    var user = req.param("user");
    var artifact = req.param("artifact");
    var auth = req.headers['Authentication'];
    if (auth) {
      sails.log.silly("Requesting addition by user %s on artifact", user, artifact);
      Heart.create({user: user, artifact: artifact}).exec(function (err, data) {
        return err ? res.status(422) : res.created
      })
    } else {
      return res.status(401)
    }
  },


  unHeartIt: function (req, res) {
    var user = req.param("user");
    var id = req.param("id");
    if (id) {
      var auth = req.headers['Authentication'];
      if (auth) {
        sails.log.silly("Requesting breakup by user %s on artifact", user, artifact);
        Heart.destroy({id: id}).exec(function (err, data) {
          return err ? res.status(422) : res.status(201)
        })
      }
    } else {
      return res.status(401)
    }
  }
};

