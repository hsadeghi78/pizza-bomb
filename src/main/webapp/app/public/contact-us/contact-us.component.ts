import { Component, OnInit } from '@angular/core';
//import { circle, latLng, polygon, tileLayer } from 'leaflet';
import * as L from 'leaflet';

const markerIcon = L.icon({
  iconSize: [25, 41],
  iconAnchor: [10, 41],
  popupAnchor: [2, -40],
  // specify the path here
  iconUrl: 'content/images/marker-icon.png',
  shadowUrl: 'content/images/marker-shadow.png',
});

@Component({
  selector: 'jhi-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.scss'],
})
export class ContactUsComponent implements OnInit {
  flMarker: L.Marker<any> | undefined;
  center: L.LatLngLiteral | L.LatLngTuple | undefined;
  // Define our base layers so we can reference them multiple times
  streetMaps = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    detectRetina: true,
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
  });
  wmMaps = L.tileLayer('http://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png', {
    detectRetina: true,
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
  });
  // Layers control object with our two base layers and the three overlay layers
  layersControl = {
    baseLayers: {
      StreetMaps: this.streetMaps,
      WikimediaMaps: this.wmMaps,
    },
    overlays: {
      'Big Circle': L.circle([32.64, 51], { radius: 5000 }),
      'Big Square': L.polygon([
        [32.6, 51.6],
        [32.2, 51.2],
        [32.0, 51.0],
        [31.6, 52.06],
      ]),
    },
  };

  options: any;
  private map: any;

  /*options = {
    layers: [tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 18, attribution: '...' })],
    zoom: 13,
    center: latLng(32.64171, 51.69004),
  };

  layersControl = {
    baseLayers: {
      'Open Street Map': tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 18, attribution: '...' }),
      'Open Cycle Map': tileLayer('http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png', { maxZoom: 18, attribution: '...' }),
    },
    overlays: {
      'Big Circle': circle([32.64, 51], { radius: 5000 }),
      'Big Square': polygon([
        [32.6, 51.6],
        [32.2, 51.2],
        [32.0, 51.0],
        [31.6, 52.06],
      ]),
    },
  };*/

  constructor() {
    console.warn('dddddddddddd');
  }

  ngOnInit(): void {
    this.center = L.latLng(32.64171, 51.69004);
    this.flMarker = L.marker(this.center, {
      icon: markerIcon,
    })
      .bindPopup('پیتزا بمب')
      .openPopup();

    this.options = {
      layers: [this.streetMaps, this.flMarker],
      zoom: 13,
      center: this.center,
    };
    console.warn('dddddddddddd');
  }

  onMapReady(event: L.Map): void {
    this.map = event;
  }
}
