import { Component, OnInit } from '@angular/core';
import { circle, latLng, polygon, tileLayer } from 'leaflet';

@Component({
  selector: 'jhi-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.scss'],
})
export class ContactUsComponent implements OnInit {
  options = {
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
  };

  constructor() {
    console.warn('dddddddddddd');
  }

  ngOnInit(): void {
    console.warn('dddddddddddd');
  }
}
