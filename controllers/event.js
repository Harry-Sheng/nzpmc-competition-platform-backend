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
  try {
    // Find the user by ID
    const user = await User.findById(userId)
    if (!user) {
      return response.status(404).json({ error: 'User not found' })
    }

    // Find the event by ID
    const event = await Event.findById(eventId)
    if (!event) {
      return response.status(404).json({ error: 'Event not found' })
    }

    // Check if the user is already signed up for the event
    if (user.events.includes(eventId)) {
      return response.status(400).json({ error: 'User already signed up for this event' })
    }

    // Associate the event with the user
    user.events = user.events.concat(eventId)
    await user.save()

    response.status(200).json({ message: 'User signed up for the event successfully', event })
  } catch (error) {
    console.error(error)
    response.status(500).json({ error: 'Failed to sign up for the event' })
  }
});

module.exports = eventsRouter