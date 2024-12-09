const bcrypt = require('bcrypt')
const usersRouter = require('express').Router()
const User = require('../models/user')
const jwt = require('jsonwebtoken')

usersRouter.get('/', async (request, response) => {
  const users = await User.find({})
  response.json(users)
})

usersRouter.post('/', async (request, response) => {
  const { name, email, password, role} = request.body

  const saltRounds = 10
  const passwordHash = await bcrypt.hash(password, saltRounds)

  const user = new User({
    name,
    email,
    passwordHash,
    role,
  })

  const savedUser = await user.save()

  response.status(201).json(savedUser)
})

// separate token from authorization header
const getTokenFrom = request => {
  const authorization = request.get('authorization')
  if (authorization && authorization.startsWith('Bearer ')) {
    return authorization.replace('Bearer ', '')
  }
  return null
}

usersRouter.get('/events', async (request, response) => {
  const decodedToken = jwt.verify(getTokenFrom(request), process.env.SECRET)
  if (!decodedToken.id) {
    return response.status(401).json({ error: 'token invalid' })
  }
  const user = await User.findById(decodedToken.id).populate('events');
  response.json(user.events);
})

module.exports = usersRouter