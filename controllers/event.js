const eventsRouter = require('express').Router()
const Event = require('../models/event')
const User = require('../models/user');
const jwt = require('jsonwebtoken')

eventsRouter.get('/', async (request, response) => {
  const events = await Event.find({})
  response.json(events)
})

eventsRouter.post('/', async (request, response) => {
  const { name, description, date} = request.body

  const event = new Event({
    name,
    description,
    date,
  })

  const savedEvent = await event.save()

  response.status(201).json(savedEvent)
})

// separate token from authorization header
const getTokenFrom = request => {
  const authorization = request.get('authorization')
  if (authorization && authorization.startsWith('Bearer ')) {
    return authorization.replace('Bearer ', '')
  }
  return null
}

eventsRouter.put('/:eventId/signup', async (request, response) => {
  const {eventId} = request.params

  const decodedToken = jwt.verify(getTokenFrom(request), process.env.SECRET)
  if (!decodedToken.id) {
    return response.status(401).json({ error: 'token invalid' })
  }
  const user = await User.findById(decodedToken.id)
  const event = await Event.findById(eventId)

  if (!event) {
    return response.status(404).json({ error: 'Event not found' })
  }

  if (user.events.includes(eventId)) {
    return response.status(400).json({ error: 'User already signed up for this event' })
  }

  // Associate the event with the user
  user.events = user.events.concat(eventId)
  await user.save()

  response.status(200).json({ message: 'User signed up for the event successfully', event })
});

module.exports = eventsRouter