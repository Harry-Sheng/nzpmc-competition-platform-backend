const eventsRouter = require('express').Router()
const Event = require('../models/event')

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

module.exports = eventsRouter