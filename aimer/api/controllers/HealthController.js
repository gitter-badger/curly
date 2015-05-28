module.exports = {
  health: function (req, res) {
    var health = {status: "UP"};
    res.json(health);
  }
};
