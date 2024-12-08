const eventsRouter = require('express').Router()
const Event = require('../models/event')
const User = require('../models/user');

eventsRouter.get('/', async (request, response) => {
  const events = await Event.find({})
  response.json(events)
})

eventsRouter.post('/', async (request, response) => {
  const { name, description} = request.body

  const event = new Event({
    name,
    description,
  })

  const savedEvent = await event.save()

  response.status(201).json(savedEvent)
})

eventsRouter.put('/:eventId/signup', async (request, response) => {
  const {eventId} = request.params
  const {userId} = request.body

  const user = await User.findById(userId)
  const event = await Event.findById(eventId)

  if (!user) {
    return response.status(404).json({ error: 'User not found' })
  }

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