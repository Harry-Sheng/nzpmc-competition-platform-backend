require('dotenv').config()
const express = require('express')
const morgan = require('morgan');
const cors = require('cors')
const mongoose = require('mongoose')
const User = require('./models/user')

const app = express()
app.use(express.json())
/*app.use(express.static('dist'))*/
app.use(cors())
app.use(morgan('tiny'))


morgan.token('body', (req) => {
    return JSON.stringify(req.body)
  });

app.use(morgan(':method :url :status :response-time ms - :res[content-length] :body'));


app.post('/api/register', (request, response, next) => {
    const user = new User({
      name: request.body.name,
      email: request.body.email,
      password: request.body.password,
      role: request.body.role,
      id: request.body.id,
    })
    console.log(user)
    user.save().then(result => {
      response.json(result)
      console.log(`Added ${user.name} email ${user.email} to user database`)
    }).catch(error => next(error))
})


const PORT = process.env.PORT
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`)
})

const errorHandler = (error, request, response, next) => {
  console.error(error.message)

  if (error.name === 'CastError') {
    return response.status(400).send({ error: 'malformatted id' })
  } else if (error.name === 'ValidationError') {
    return response.status(400).json({ error: error.message })
  }

  next(error)
}

// this has to be the last loaded middleware, also all the routes should be registered before this!
app.use(errorHandler)