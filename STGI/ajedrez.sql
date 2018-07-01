-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 01, 2018 at 05:40 PM
-- Server version: 5.7.22-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ajedrez`
--

-- --------------------------------------------------------

--
-- Table structure for table `detallesjugadorpartido`
--

CREATE TABLE `detallesjugadorpartido` (
  `partido` int(11) NOT NULL,
  `jugador` int(11) NOT NULL,
  `color` tinyint(1) NOT NULL,
  `enjaque` tinyint(1) NOT NULL DEFAULT '0',
  `resultado` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `estatisticasusuarios`
--

CREATE TABLE `estatisticasusuarios` (
  `usuario` int(11) NOT NULL,
  `jugados` int(11) DEFAULT '0',
  `victorias` int(11) NOT NULL DEFAULT '0',
  `derrotas` int(11) NOT NULL DEFAULT '0',
  `establos` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `historiapartido`
--

CREATE TABLE `historiapartido` (
  `partido` int(11) NOT NULL,
  `pieza` int(11) NOT NULL,
  `color` tinyint(1) NOT NULL,
  `letrainicial` char(1) NOT NULL,
  `numeroinicial` char(1) NOT NULL,
  `letrafinal` char(1) NOT NULL,
  `numerofinal` char(1) NOT NULL,
  `come` int(11) DEFAULT NULL,
  `evoluciona` int(11) DEFAULT NULL,
  `jaque` tinyint(1) DEFAULT '0',
  `jaquemate` tinyint(1) DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `listadopartidos`
--

CREATE TABLE `listadopartidos` (
  `id` int(11) NOT NULL,
  `turno` int(11) NOT NULL,
  `terminato` tinyint(1) NOT NULL DEFAULT '0',
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `partidosdisponibles`
--

CREATE TABLE `partidosdisponibles` (
  `id` int(11) NOT NULL,
  `jugador` int(11) NOT NULL,
  `color` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `piezas`
--

CREATE TABLE `piezas` (
  `id` int(11) NOT NULL,
  `nombre` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `piezas`
--

INSERT INTO `piezas` (`id`, `nombre`) VALUES
(1, 'Peone'),
(2, 'Torre'),
(3, 'Caballo'),
(4, 'Alfile'),
(5, 'Dama'),
(6, 'Rey');

-- --------------------------------------------------------

--
-- Table structure for table `tablapartido`
--

CREATE TABLE `tablapartido` (
  `partido` int(11) NOT NULL,
  `pieza` int(11) NOT NULL,
  `letra` char(1) NOT NULL,
  `numero` char(1) NOT NULL,
  `avanzo` tinyint(1) NOT NULL DEFAULT '0',
  `color` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `nombre` text NOT NULL,
  `apellido` text NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detallesjugadorpartido`
--
ALTER TABLE `detallesjugadorpartido`
  ADD UNIQUE KEY `partido` (`partido`,`jugador`);

--
-- Indexes for table `estatisticasusuarios`
--
ALTER TABLE `estatisticasusuarios`
  ADD KEY `usuario` (`usuario`);

--
-- Indexes for table `listadopartidos`
--
ALTER TABLE `listadopartidos`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `partidosdisponibles`
--
ALTER TABLE `partidosdisponibles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `piezas`
--
ALTER TABLE `piezas`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique` (`username`(20));

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `listadopartidos`
--
ALTER TABLE `listadopartidos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
--
-- AUTO_INCREMENT for table `partidosdisponibles`
--
ALTER TABLE `partidosdisponibles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `piezas`
--
ALTER TABLE `piezas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `estatisticasusuarios`
--
ALTER TABLE `estatisticasusuarios`
  ADD CONSTRAINT `estatisticasusuarios_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
