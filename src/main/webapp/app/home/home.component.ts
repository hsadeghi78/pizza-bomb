import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { NgImageSliderComponent } from 'ng-image-slider';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild('nav', { static: false })
  slider?: NgImageSliderComponent;

  account: Account | null = null;
  authSubscription?: Subscription;
  imageObject = [
    {
      image: '../../content/images/slider.jpg',
      thumbImage: '../../content/images/slider.jpg',
    },
    {
      image: '../../content/images/slider2.jpg',
      thumbImage: '../../content/images/slider2.jpg',
    },
    {
      image: '../../content/images/slider01.jpg',
      thumbImage: '../../content/images/slider01.jpg',
    },
    {
      image: '../../content/images/slider3.jpg',
      thumbImage: '../../content/images/slider3.jpg',
    },
  ];

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  prevImageClick(): void {
    this.slider?.prev();
  }

  nextImageClick(): void {
    this.slider?.next();
  }
}
