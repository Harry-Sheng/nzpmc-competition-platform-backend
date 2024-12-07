const userRouter = require('express').Router()
const User = require('../models/user')

userRouter.post('/register', (request, response, next) => {
    const user = new User({
      name: request.body.name,
      email: request.body.email,
      password: request.body.password,
      role: request.body.role,
      id: request.body.id,
    })
    user.save().then(result => {
      response.json(result)
    }).catch(error => next(error))
})

module.exports = userRouter